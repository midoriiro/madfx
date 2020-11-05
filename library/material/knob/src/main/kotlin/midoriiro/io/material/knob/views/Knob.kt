package midoriiro.io.material.knob.views

import android.animation.FloatEvaluator
import android.animation.TimeInterpolator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import midoriiro.io.core.colors.Palette
import midoriiro.io.core.components.RelativeBounds
import midoriiro.io.core.extensions.*
import midoriiro.io.core.gestures.GestureDetector
import midoriiro.io.core.gestures.SimpleOnGestureListener
import midoriiro.io.core.utils.MathUtils
import midoriiro.io.material.knob.R
import midoriiro.io.material.knob.enums.GestureOrientation
import midoriiro.io.material.knob.formatters.KnobDefaultLabelFormatter
import midoriiro.io.material.knob.formatters.KnobDefaultValueFormatter
import midoriiro.io.material.knob.interfaces.KnobLabelFormatter
import midoriiro.io.material.knob.interfaces.KnobValueFormatter

class Knob : View
{
	private inner class GestureListener : SimpleOnGestureListener()
	{
		private fun onOneAxis(delta: Float): Float
		{
			val angle = this@Knob._valueEvaluator.fromValue(this@Knob._value) + delta
			return this@Knob._valueEvaluator.fromAngle(angle.clamp(MINIMUM_ANGLE, MAXIMUM_ANGLE))
		}

		private fun onTwoAxis(event: MotionEvent): Float
		{
			val angle = MathUtils
				.angle(
					this@Knob._traceMinimum.x,
					this@Knob._traceMinimum.y,
					event.x,
					event.y,
					this@Knob._bounds.halfWidth,
					this@Knob._bounds.halfHeight
				)
				.toClockwise()
				.plus(MINIMUM_ANGLE)
			return when (angle.sign(MAXIMUM_ANGLE, MAXIMUM_ANGLE + DEAD_ZONE_SWEEP_ANGLE, HALF_DEAD_ZONE_SWEEP_ANGLE))
			{
				1f -> this@Knob._valueEvaluator.fromAngle(MINIMUM_ANGLE)
				-1f -> this@Knob._valueEvaluator.fromAngle(MAXIMUM_ANGLE)
				else -> this@Knob._valueEvaluator.fromAngle(angle)
			}
		}

		override fun onUp(event: MotionEvent): Boolean
		{
			this@Knob.requestDisallowParentTouchEvent(false)
			return true
		}

		override fun onDown(event: MotionEvent): Boolean
		{
			this@Knob.requestDisallowParentTouchEvent(true)
			return true
		}

		override fun onSingleTapConfirmed(event: MotionEvent): Boolean
		{
			val value = this@Knob._value
			this@Knob._value = this.onTwoAxis(event)
			this@Knob.clamp()
			this@Knob._animator.setFloatValues(value, this@Knob._value)
			this@Knob._animator.start()
			return true
		}

		override fun onScroll(
			event1: MotionEvent,
			event2: MotionEvent,
			distanceX: Float,
			distanceY: Float
		): Boolean
		{
			this@Knob._value = when (this@Knob._gestureOrientation)
			{
				GestureOrientation.Horizontal -> this.onOneAxis(-distanceX)
				GestureOrientation.Vertical -> this.onOneAxis(distanceY)
				GestureOrientation.Circular -> this.onTwoAxis(event2)
			}
			this@Knob.clamp()
			this@Knob.invalidate()
			return true
		}

		override fun onDoubleTapEvent(event: MotionEvent): Boolean
		{
			val value = this@Knob._value
			this@Knob._value = this@Knob._defaultValue
			this@Knob.clamp()
			this@Knob._animator.setFloatValues(value, this@Knob._value)
			this@Knob._animator.start()
			return true
		}
	}

	private inner class ValueEvaluator
	{
		private var _step: Float? = null
		private var _valueRanges: List<Pair<Float, Float>>? = null
		private var _angleRanges: List<Pair<Float, Float>>? = null

		private fun getStep(): Float
		{
			if(this._step != null)
			{
				return this._step!!
			}
			this._step = 1f / (this@Knob._middleValues!!.size + 1f)
			return this._step!!
		}

		private fun getValueRanges(): List<Pair<Float, Float>>
		{
			if(this._valueRanges != null)
			{
				return this._valueRanges!!
			}
			val valueRanges = mutableListOf<Pair<Float, Float>>()
			for(index in 1 until this@Knob._middleValues!!.size)
			{
				val minimum = this@Knob._middleValues!![index - 1]
				val maximum = this@Knob._middleValues!![index]
				valueRanges.add(minimum to maximum)
			}
			valueRanges.add(0, this@Knob._minimumValue to this@Knob._middleValues!!.first())
			valueRanges.add(this@Knob._middleValues!!.last() to this@Knob._maximumValue)
			this._valueRanges = valueRanges
			return this._valueRanges!!
		}

		private fun getAngleRanges(): List<Pair<Float, Float>>
		{
			if(this._angleRanges != null)
			{
				return this._angleRanges!!
			}
			this._angleRanges = this.getValueRanges().map { this.getAngles(it) }
			return this._angleRanges!!
		}

		private fun getAngles(range: Pair<Float, Float>): Pair<Float, Float>
		{
			val minimum = this.getAngle(range.first)
			val maximum = this.getAngle(range.second)
			return minimum to maximum
		}

		private fun getAngle(value: Float): Float
		{
			val index = this@Knob._middleValues!!.indexOfFirst { it == value }
			val position = when (value)
			{
				this@Knob._minimumValue -> 0f
				this@Knob._maximumValue -> 1f
				else -> (index + 1f) * this.getStep()
			}
			return MathUtils.map(
				position,
				0f,
				1f,
				MINIMUM_ANGLE,
				MAXIMUM_ANGLE
			)
		}

		fun fromValue(value: Float): Float
		{
			if(this@Knob._middleValues == null)
			{
				return MathUtils.map(
					value,
					this@Knob._minimumValue,
					this@Knob._maximumValue,
					MINIMUM_ANGLE,
					MAXIMUM_ANGLE
				)
			}
			val range = this.getValueRanges().first { it.first <= value && value <= it.second }
			val angles = this.getAngles(range)
			return MathUtils.map(
				value,
				range.first,
				range.second,
				angles.first,
				angles.second
			)
		}

		fun fromAngle(angle: Float): Float
		{
			if(this@Knob._middleValues == null)
			{
				return MathUtils.map(
					angle,
					MINIMUM_ANGLE,
					MAXIMUM_ANGLE,
					this@Knob._minimumValue,
					this@Knob._maximumValue
				)
			}
			val ranges = this.getValueRanges()
			val angles = this.getAngleRanges().first { it.first <= angle && angle <= it.second }
			val index = this.getAngleRanges().indexOf(angles)
			val range = ranges[index]
			return MathUtils.map(
				angle,
				angles.first,
				angles.second,
				range.first,
				range.second
			)
		}
	}

	private companion object
	{
		const val MINIMUM_ANGLE = 135f
		const val MAXIMUM_ANGLE = 405f
		const val DEAD_ZONE_SWEEP_ANGLE = 360f - (MAXIMUM_ANGLE - MINIMUM_ANGLE)
		const val HALF_DEAD_ZONE_SWEEP_ANGLE = DEAD_ZONE_SWEEP_ANGLE / 2F
		const val MINIMUM_SWEEP_ANGLE = 0f
		const val MAXIMUM_SWEEP_ANGLE = 270f
	}

	private val _gestureListener = GestureListener()
	private val _gestureDetector = GestureDetector(this.context, this._gestureListener)
	private val _valueEvaluator = ValueEvaluator()
	private val _animator = ValueAnimator()
	private val _palette = Palette(this)
	private val _painter = Paint(Paint.ANTI_ALIAS_FLAG)
	private val _bounds = RelativeBounds()
	private val _bubbleOrigin = PointF()
	private val _traceMinimum = PointF()
	private val _traceMaximum = PointF()
	private var _traceStrokeWidth = 0f
	private var _bubbleSize = 0f
	private var _minimumValue = 0f
	private var _maximumValue = 0f
	private var _middleValues: FloatArray? = null
	private var _value = 0f
	private var _defaultValue = 0f
	private var _name: String? = null
	private var _nameSize = 0f
	private var _labelSize = 0f
	private lateinit var _valueFormatter: KnobValueFormatter
	private lateinit var _labelFormatter: KnobLabelFormatter
	private var _gestureOrientation = GestureOrientation.Horizontal
	private var _animationDuration = 0L
	private lateinit var _animationInterpolator: TimeInterpolator
	private lateinit var _animationEvaluator: TypeEvaluator<*>

	constructor(
		context: Context, attrs: AttributeSet
	) : super(context, attrs)
	{
		this.init(attrs, 0)
	}

	constructor(
		context: Context, attrs: AttributeSet, defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.init(attrs, defStyleAttr)
	}

	constructor(
		context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.init(attrs, defStyleAttr)
	}

	private fun init(attrs: AttributeSet, defStyle: Int)
	{
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.Knob, defStyle, 0
		)

		this.isEnabled = typedArray.getBoolean(
			R.styleable.Knob_android_enabled,
			this.isEnabled
		)

		this._traceStrokeWidth = typedArray.getDimension(
			R.styleable.Knob_traceStrokeWidth,
			4f.fromDp() // TODO move default values to another place
		)

		this._bubbleSize = typedArray.getDimension(
			R.styleable.Knob_bubbleSize,
			8f.fromDp()
		)

		this._minimumValue = typedArray.getFloat(
			R.styleable.Knob_minimumValue,
			0f
		)

		this._maximumValue = typedArray.getFloat(
			R.styleable.Knob_maximumValue,
			100f
		)

		this._middleValues = typedArray
			.getFloatArrayOrNull(
				R.styleable.Knob_middleValues
			)
			?.clamp(this._minimumValue, this._maximumValue, false)
			?.sort()

		this._value = typedArray.getFloat(
			R.styleable.Knob_value,
			50f
		)

		this._defaultValue = typedArray.getFloat(
			R.styleable.Knob_defaultValue,
			50f
		)

		this._name = typedArray.getString(
			R.styleable.Knob_name
		)

		this._nameSize = typedArray.getDimension(
			R.styleable.Knob_nameSize,
			14f.fromSp()
		)

		this._labelSize = typedArray.getDimension(
			R.styleable.Knob_labelSize,
			14f.fromSp()
		)

		this._valueFormatter = typedArray.getClass(
			R.styleable.Knob_valueFormatter,
			KnobDefaultValueFormatter::class
		)

		this._labelFormatter = typedArray.getClass(
			R.styleable.Knob_labelFormatter,
			KnobDefaultLabelFormatter::class
		)

		this._gestureOrientation = typedArray.getEnum(
			R.styleable.Knob_gestureOrientation,
			GestureOrientation.Horizontal
		)

		this._animationDuration = typedArray.getLong(
			R.styleable.Knob_animationDuration,
			300
		)

		this._animationInterpolator = typedArray.getClass(
			R.styleable.Knob_animationInterpolator,
			FastOutSlowInInterpolator::class
		)

		this._animationEvaluator = typedArray.getClass(
			R.styleable.Knob_animationEvaluator,
			FloatEvaluator::class
		)

		typedArray.recycle()

		this._animator.duration = this._animationDuration
		this._animator.interpolator = this._animationInterpolator
		this._animator.setEvaluator(this._animationEvaluator)
		this._animator.addUpdateListener { animator ->
			val value = animator.animatedValue as Float
			this._value = value
			this.clamp()
			this.invalidate()
		}
	}

	private fun requestDisallowParentTouchEvent(state: Boolean)
	{
		if(this.parent != null)
		{
			this.parent.requestDisallowInterceptTouchEvent(state)
		}
	}

	private fun clamp()
	{
		when
		{
			this@Knob._value > this@Knob._maximumValue -> this@Knob._value = this@Knob._maximumValue
			this@Knob._value < this@Knob._minimumValue -> this@Knob._value = this@Knob._minimumValue
		}
	}

	override fun onDetachedFromWindow()
	{
		super.onDetachedFromWindow()
		this._animator.cancel()
	}

	override fun onSizeChanged(
		w: Int,
		h: Int,
		oldw: Int,
		oldh: Int
	)
	{
		super.onSizeChanged(w, h, oldw, oldh)
		this._bounds.setBounds(
			this,
			0f,
			0f,
			0f,
			0f
		)
		this._bubbleOrigin.set(MathUtils.rotate(
			this._bounds.widthWithPadding,
			this._bounds.halfHeight,
			this._bounds.halfWidth,
			this._bounds.halfHeight,
			0f
		))
		this._traceMinimum.set(MathUtils.rotate(
			this._bubbleOrigin.x,
			this._bubbleOrigin.y,
			this._bounds.halfWidth,
			this._bounds.halfHeight,
			MINIMUM_ANGLE
		))
		this._traceMaximum.set(MathUtils.rotate(
			this._bubbleOrigin.x,
			this._bubbleOrigin.y,
			this._bounds.halfWidth,
			this._bounds.halfHeight,
			MAXIMUM_ANGLE
		))
	}

	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		return when
		{
			!this.isEnabled -> false
			this._gestureDetector.onTouchEvent(event) -> true
			else -> super.onTouchEvent(event)
		}
	}

	override fun onDraw(canvas: Canvas)
	{
		this.onDrawMiddlePointsLabel(canvas)
		this.onDrawBackgroundTrace(canvas)
		this.onDrawForegroundTrace(canvas)
		this.onDrawBubble(canvas)
		this.onDrawName(canvas)
		this.onDrawMinimumValueLabel(canvas)
		this.onDrawMaximumValueLabel(canvas)
		this.onDrawValue(canvas)
	}

	private fun onDrawBackgroundTrace(canvas: Canvas)
	{
		this._painter.style = Paint.Style.STROKE
		this._painter.color = this._palette.getStrokeColorFromState(this._palette.onSurface.toLowEmphasis())
		this._painter.strokeWidth = this._traceStrokeWidth
		this._painter.strokeCap = Paint.Cap.ROUND
		canvas.drawArc(
			this._bounds.relativeRectangleWithPadding,
			MINIMUM_ANGLE,
			MAXIMUM_SWEEP_ANGLE,
			false,
			this._painter
		)
	}

	private fun onDrawForegroundTrace(canvas: Canvas)
	{
		this._painter.style = Paint.Style.STROKE
		this._painter.color = this._palette.getStrokeColorFromState(this._palette.primary)
		this._painter.strokeWidth = this._traceStrokeWidth
		this._painter.strokeCap = Paint.Cap.ROUND
		val angle = MathUtils.map(
			this._valueEvaluator.fromValue(this._value),
			MINIMUM_ANGLE,
			MAXIMUM_ANGLE,
			MINIMUM_SWEEP_ANGLE,
			MAXIMUM_SWEEP_ANGLE
		)
		canvas.drawArc(
			this._bounds.relativeRectangleWithPadding,
			MINIMUM_ANGLE,
			angle,
			false,
			this._painter
		)
	}

	private fun onDrawBubble(canvas: Canvas)
	{
		if(!this.isEnabled)
		{
			return
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getStrokeColorFromState(this._palette.primary)
		val angle = this._valueEvaluator.fromValue(this._value)
		val point = MathUtils.rotate(
			this._bubbleOrigin.x,
			this._bubbleOrigin.y,
			this._bounds.halfWidth,
			this._bounds.halfHeight,
			angle
		)
		canvas.drawCircle(
			point.x,
			point.y,
			this._bubbleSize,
			this._painter
		)
	}

	private fun onDrawName(canvas: Canvas)
	{
		if(this._name == null)
		{
			return
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._nameSize
		this._painter.textAlign = Paint.Align.LEFT
		var point = this._painter.textAlignCenter(
			this._bounds.halfWidth,
			this._bounds.halfHeight,
			this._name!!
		)
		point = this._painter.textAlignVerticalCenter(
			point.x,
			point.y,
			this._name!!
		)
		canvas.drawText(this._name!!, point.x, point.y, this._painter)
	}

	private fun onDrawMinimumValueLabel(canvas: Canvas)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		val string = this._labelFormatter.minimum(this._minimumValue)
		var point = this._painter.textAlignCenter(
			this._traceMinimum.x,
			(this.height - this._traceMinimum.y) / 2f + this._traceMinimum.y,
			string
		)
		point = this._painter.textAlignVerticalCenter(
			point.x,
			point.y,
			string
		)
		canvas.drawText(string, point.x, point.y, this._painter)
	}

	private fun onDrawMaximumValueLabel(canvas: Canvas)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		val string = this._labelFormatter.minimum(this._maximumValue)
		var point = this._painter.textAlignCenter(
			this._traceMaximum.x,
			(this.height - this._traceMaximum.y) / 2f + this._traceMaximum.y,
			string
		)
		point = this._painter.textAlignVerticalCenter(
			point.x,
			point.y,
			string
		)
		canvas.drawText(string, point.x, point.y, this._painter)
	}

	private fun onDrawMiddlePointsLabel(canvas: Canvas)
	{
		if(this._middleValues == null)
		{
			return
		}
		this._painter.style = Paint.Style.STROKE
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		val step = 1f / (this._middleValues!!.size + 1f)
		val center = PointF(this._bounds.halfWidth, this._bounds.halfHeight)
		for(index in this._middleValues!!.indices)
		{
			this._painter.style = Paint.Style.STROKE
			this._painter.color = this._palette.getStrokeColorFromState(this._palette.onSurface.toLowEmphasis())
			this._painter.strokeWidth = this._traceStrokeWidth
			this._painter.strokeCap = Paint.Cap.SQUARE
			val position = (index + 1f) * step
			val angle = MathUtils.map(
				position,
				0f,
				1f,
				MINIMUM_ANGLE,
				MAXIMUM_ANGLE
			)
			val origin = MathUtils
				.rotate(
					this._bubbleOrigin.x,
					this._bubbleOrigin.y,
					this._bounds.halfWidth,
					this._bounds.halfHeight,
					angle
				)
				.distance(center, this._traceStrokeWidth)
			val tick = origin.distance(center, this._bubbleSize)
			canvas.drawLine(origin.x, origin.y, tick.x, tick.y, this._painter)
			this._painter.style = Paint.Style.FILL
			this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
			this._painter.textSize = this._labelSize
			this._painter.textAlign = Paint.Align.LEFT
			val string = this._labelFormatter.minimum(this._middleValues!![index])
			val bounds = this._painter.textBounds(string)
			val diagonal = PointF(bounds.right - bounds.centerX(), bounds.bottom - bounds.centerY()).magnitude()
			val length = diagonal + this._bubbleSize
			var label = tick.distance(center, length)
			label = this._painter.textAlignCenter(
				label.x,
				label.y,
				string
			)
			label = this._painter.textAlignVerticalCenter(
				label.x,
				label.y,
				string
			)
			canvas.drawText(string, label.x, label.y, this._painter)
		}
	}

	private fun onDrawValue(canvas: Canvas)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		val string = this._valueFormatter.format(this._value)
		var point = this._painter.textAlignCenter(
			this._bounds.halfWidth,
			this._traceMinimum.y,
			string
		)
		point = this._painter.textAlignVerticalCenter(
			point.x,
			point.y,
			string
		)
		canvas.drawText(string, point.x, point.y, this._painter)
	}
}