package midoriiro.io.material.knob.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import midoriiro.io.core.colors.Palette
import midoriiro.io.core.components.RelativeBounds
import midoriiro.io.core.extensions.*
import midoriiro.io.core.utils.MathUtils
import midoriiro.io.material.knob.R
import midoriiro.io.material.knob.formatters.KnobDefaultValueFormatter
import midoriiro.io.material.knob.interfaces.KnobValueFormatter

class Knob : View
{
	private companion object
	{
		const val MINIMUM_ANGLE = 135f
		const val MAXIMUM_ANGLE = 405f
		const val MINIMUM_SWEEP_ANGLE = 0f
		const val MAXIMUM_SWEEP_ANGLE = 270f
	}

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
	private var _value = 0f
	private var _name: String? = null
	private var _nameSize = 0f
	private var _minimumValueLabel: String? = null
	private var _maximumValueLabel: String? = null
	private var _labelSize = 0f
	private lateinit var _formatter: KnobValueFormatter

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
		val theme = this.context.theme

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

		this._value = typedArray.getFloat(
			R.styleable.Knob_value,
			50f
		)

		this._name = typedArray.getString(
			R.styleable.Knob_name
		)

		this._nameSize = typedArray.getDimension(
			R.styleable.Knob_nameSize,
			14f.fromSp()
		)

		this._minimumValueLabel = typedArray.getString(
			R.styleable.Knob_minimumValueLabel
		)

		this._maximumValueLabel = typedArray.getString(
			R.styleable.Knob_maximumValueLabel
		)

		this._labelSize = typedArray.getDimension(
			R.styleable.Knob_labelSize,
			14f.fromSp()
		)

		val formatterClass = typedArray.getString(
			R.styleable.Knob_formatter
		)

		if(formatterClass.isNullOrEmpty())
		{
			this._formatter = KnobDefaultValueFormatter()
		}
		else
		{
			this._formatter = KnobValueFormatter::class.createFromString(formatterClass)
		}

		typedArray.recycle()
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

	override fun onDraw(canvas: Canvas)
	{
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
			this._value,
			this._minimumValue,
			this._maximumValue,
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
		val angle = MathUtils.map(
			this._value,
			this._minimumValue,
			this._maximumValue,
			MINIMUM_ANGLE,
			MAXIMUM_ANGLE
		)
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
		if(this._minimumValueLabel == null)
		{
			return
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		var point = this._painter.textAlignCenter(
			this._traceMinimum.x,
			(this.height - this._traceMinimum.y) / 2f + this._traceMinimum.y,
			this._minimumValueLabel!!
		)
		point = this._painter.textAlignVerticalCenter(
			point.x,
			point.y,
			this._minimumValueLabel!!
		)
		canvas.drawText(this._minimumValueLabel!!, point.x, point.y, this._painter)
	}

	private fun onDrawMaximumValueLabel(canvas: Canvas)
	{
		if(this._maximumValueLabel == null)
		{
			return
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		var point = this._painter.textAlignCenter(
			this._traceMaximum.x,
			(this.height - this._traceMaximum.y) / 2f + this._traceMaximum.y,
			this._maximumValueLabel!!
		)
		point = this._painter.textAlignVerticalCenter(
			point.x,
			point.y,
			this._maximumValueLabel!!
		)
		canvas.drawText(this._maximumValueLabel!!, point.x, point.y, this._painter)
	}

	private fun onDrawValue(canvas: Canvas)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._palette.getTextColorFromState(this._palette.onSurface.toMediumEmphasis())
		this._painter.textSize = this._labelSize
		this._painter.textAlign = Paint.Align.LEFT
		val string = this._formatter.format(this._value)
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