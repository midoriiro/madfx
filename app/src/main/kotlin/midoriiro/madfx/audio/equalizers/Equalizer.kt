package midoriiro.madfx.audio.equalizers

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import midoriiro.madfx.R
import midoriiro.madfx.core.Application
import midoriiro.madfx.core.collections.LineArray
import midoriiro.madfx.core.colors.Rainbow
import midoriiro.madfx.core.extensions.*
import midoriiro.madfx.core.utils.MathUtils
import kotlin.math.log10
import kotlin.math.pow

class Equalizer : View
{
	private class Bounds
	{
		companion object
		{
			private val _bounds = RectF()
			private var _width = 0f
			private var _widthWithPadding = 0f
			private var _halfWidth = 0f
			private var _height = 0f
			private var _heightWithPadding = 0f
			private var _halfHeight = 0f
			private var _heightTwoThird = 0f
			private var _heightTwoThirdWithPadding = 0f
			private var _halfHeightTwoThird = 0f
			private var _halfHeightTwoThirdWithPadding = 0f
			private var _top = 0f
			private var _topWithPadding = 0f
			private var _bottom = 0f
			private var _bottomWithPadding = 0f
			private var _left = 0f
			private var _leftWithPadding = 0f
			private var _right = 0f
			private var _rightWithPadding = 0f
			
			val width: Float
				get() = this._width
			
			val widthWithPadding: Float
				get() = this._widthWithPadding

			val halfWidth: Float
				get() = this._halfWidth
			
			val height: Float
				get() = this._height
			
			val heightWithPadding: Float
				get() = this._heightWithPadding

			val halfHeight: Float
				get() = this._halfHeight
			
			val heightTwoThird: Float
				get() = this._heightTwoThird

			val heightTwoThirdWithPadding: Float
				get() = this._heightTwoThirdWithPadding
			
			val halfHeightTwoThird: Float
				get() = this._halfHeightTwoThird

			val halfHeightTwoThirdWithPadding: Float
				get() = this._halfHeightTwoThirdWithPadding
			
			val top: Float
				get() = this._top
			
			val topWithPadding: Float
				get() = this._topWithPadding
			
			val bottom: Float
				get() = this._bottom
			
			val bottomWithPadding: Float
				get() = this._bottomWithPadding
			
			val left: Float
				get() = this._left
			
			val leftWithPadding: Float
				get() = this._leftWithPadding
			
			val right: Float
				get() = this._right
			
			val rightWithPadding: Float
				get() = this._rightWithPadding
			
			fun setBounds(
				view: View,
				paddingLeft: Float,
				paddingTop: Float,
				paddingRight: Float,
				paddingBottom: Float
			)
			{
				val minX = (view.left + view.paddingLeft).toFloat()
				val maxX = (view.width).toFloat()
				val minY = (view.top + view.paddingTop).toFloat()
				val maxY = (view.height).toFloat()
				this._bounds.set(minX, minY, maxX, maxY)
				this._width = this._bounds.width()
				this._widthWithPadding = this._width - paddingRight
				this._halfWidth = this._width / 2f
				this._height = this._bounds.height()
				this._heightWithPadding = this._height - paddingBottom
				this._halfHeight = this._height / 2f
				this._top = this._bounds.top
				this._topWithPadding = this._top + paddingTop
				this._bottom = this._bounds.bottom
				this._bottomWithPadding = this._bottom - paddingBottom
				this._left = this._bounds.left
				this._leftWithPadding = this._left + paddingLeft
				this._right = this._bounds.right
				this._rightWithPadding = this._right - paddingRight
				this._heightTwoThird = this._height / 3f * 2f
				this._heightTwoThirdWithPadding = this._heightWithPadding / 3f * 2f
				this._halfHeightTwoThird = this._heightTwoThird / 2f + paddingTop / 2f
				this._halfHeightTwoThirdWithPadding = this._heightTwoThirdWithPadding / 2f + this._topWithPadding / 2f
			}
		}
	}
	
	private class Limits
	{
		companion object
		{
			private var _frequencyMinimum = 10
			private var _frequencyMaximum = 20000
			private var _frequencyFloatMinimum = 0f
			private var _frequencyFloatMaximum = 0f
			private var _frequencyLog10Minimum = 0f
			private var _frequencyLog10Maximum = 0f
			
			private var _gainMinimum = -12
			private var _gainMaximum = 12
			private var _gainFloatMinimum = 0f
			private var _gainFloatMaximum = 0f

			private var _resolution = 256
			private var _resolutionFloat = 0f
			
			private var _frequencies = mutableListOf<Float>()
			
			var frequencyMinimum: Int
				set(value)
				{
					this._frequencyMinimum = value
					this._frequencyFloatMinimum = value.toFloat()
					this._frequencyLog10Minimum = log10(this._frequencyFloatMinimum)
				}
				get() = this._frequencyMinimum
			
			val frequencyFloatMinimum: Float
				get() = this._frequencyFloatMinimum
			
			val frequencyLog10Minimum: Float
				get() = this._frequencyLog10Minimum
			
			var frequencyMaximum: Int
				set(value)
				{
					this._frequencyMaximum = value
					this._frequencyFloatMaximum = value.toFloat()
					this._frequencyLog10Maximum = log10(this._frequencyFloatMaximum)
				}
				get() = this._frequencyMaximum
			
			val frequencyFloatMaximum: Float
				get() = this._frequencyFloatMaximum
			
			val frequencyLog10Maximum: Float
				get() = this._frequencyLog10Maximum
			
			var gainMinimum: Int
				set(value)
				{
					this._gainMinimum = value
					this._gainFloatMinimum = value.toFloat()
				}
				get() = this._gainMinimum
			
			val gainFloatMinimum: Float
				get() = this._gainFloatMinimum
			
			var gainMaximum: Int
				set(value)
				{
					this._gainMaximum = value
					this._gainFloatMaximum = value.toFloat()
				}
				get() = this._gainMaximum
			
			val gainFloatMaximum: Float
				get() = this._gainFloatMaximum
			
			var resolution: Int
				set(value)
				{
					this._resolution = value
					this._resolutionFloat = value.toFloat()
					this._frequencies.clear()
					for (exponent in 1..5)
					{
						val minimum = 10f.pow(exponent.toFloat() - 1)
						var maximum = 10f.pow(exponent.toFloat())
						if(maximum > 44100f)
						{
							maximum = 44100f
						}
						for(index in 0..this._resolution)
						{
							val frequency = MathUtils.map(
								index.toFloat(),
								0f,
								this._resolutionFloat,
								minimum,
								maximum
							)
							this._frequencies.add(frequency)
						}
					}
				}
				get() = this._resolution
			
			val resolutionFloat: Float
				get() = this._resolutionFloat
			
			val frequencies: List<Float>
				get() = this._frequencies.toList()
			
			init
			{
				this.frequencyMinimum = this._frequencyMinimum
				this.frequencyMaximum = this._frequencyMaximum
				this.gainMinimum = this._gainMinimum
				this.gainMaximum = this._gainMaximum
				this.resolution = this._resolution
			}
		}
	}
	
	private class TicksXAxis
	{
		private val _ticks = listOf(
			10f, 20f, 30f, 40f, 50f, 60f, 70f, 80f, 90f,
			100f, 200f, 300f, 400f, 500f, 600f, 700f, 800f, 900f,
			1000f, 2000f, 3000f, 4000f, 5000f, 6000f, 7000f, 8000f, 9000f,
			10000f, 20000f
		)
		
		private val _ticksLog10 = this._ticks.map { log10(it) }
		private val _ticksLog10Minimum = this._ticksLog10.minOrNull()!!
		private val _ticksLog10Maximum = this._ticksLog10.maxOrNull()!!
		
		private val _labels = listOf(
			10f, 20f, 50f,
			100f, 200f, 500f,
			1000f, 2000f, 5000f,
			10000f, 20000f,
		)
		
		private val _labelsString = this._labels
			.map {
				if(it >= 1000)
				{
					return@map (it / 1000).toInt().toString() + "k"
				}
				
				return@map it.toInt().toString()
			}
			.toList()
		private val _labelsLog10 = this._labels.map { log10(it) }
		private val _labelsLog10Minimum = this._labelsLog10.minOrNull()!!
		private val _labelsLog10Maximum = this._labelsLog10.maxOrNull()!!
		
		val ticks: List<Float>
			get() = this._ticksLog10
		
		val ticksMinimum: Float
			get() = this._ticksLog10Minimum
		
		val ticksMaximum: Float
			get() = this._ticksLog10Maximum
			
		val labels: List<Float>
			get() = this._labelsLog10
		
		val labelsString: List<String>
			get() = this._labelsString
		
		val labelsMinimum: Float
			get() = this._labelsLog10Minimum
		
		val labelsMaximum: Float
			get() = this._labelsLog10Maximum

		val points: List<Float>
			get() {
				val list = mutableListOf<Float>()
				for(index in this.ticks.indices)
				{
					val tick = this.ticks[index]
					val x = MathUtils.map(
						tick,
						this.ticksMinimum,
						this.ticksMaximum,
						Bounds.leftWithPadding,
						Bounds.widthWithPadding
					)
					list.add(x)
				}
				return list.toList()
			}
	}
	
	private class TicksYAxis
	{
		private val _ticks = listOf(
			//30f, 27f,
			//24f, 21f, 18f, 15f,
			12f, 9f, 6f, 3f,
			0f,
			-3f, -6f, -9f, -12f,
//			-15f, -18f, -21f, -24f,
//			-27f, -30f
		)
		private val _ticksMinimum = this._ticks.minOrNull()!!
		private val _ticksMaximum = this._ticks.maxOrNull()!!
		
		val ticks: List<Float>
			get() = this._ticks
		
		val ticksMinimum: Float
			get() = this._ticksMinimum
		
		val ticksMaximum: Float
			get() = this._ticksMaximum

		val points: List<Float>
			get() {
				val list = mutableListOf<Float>()
				for(index in this.ticks.indices.reversed())
				{
					val tick = this.ticks[index]
					val y = MathUtils.map(
						tick,
						this.ticksMinimum,
						this.ticksMaximum,
						Bounds.topWithPadding,
						Bounds.heightTwoThirdWithPadding
					)
					list.add(y)
				}
				return list.toList()
			}
	}
	
	private class Band(
		color: Int,
		enabled: Boolean,
		selected: Boolean,
		type: BiQuadraticFilter.Type,
		frequency: Double,
		gain: Double,
		width: Double,
		rate: Double
	)
	{
		companion object
		{
			fun compute(frequency: Float, amplitude: Float): PointF
			{
				val x = MathUtils.map(
					log10(frequency),
					Limits.frequencyLog10Minimum,
					Limits.frequencyLog10Maximum,
					Bounds.leftWithPadding,
					Bounds.widthWithPadding
				)
				val y = MathUtils.map(
					-amplitude,
					Limits.gainFloatMinimum,
					Limits.gainFloatMaximum,
					Bounds.topWithPadding,
					Bounds.heightTwoThirdWithPadding
				)
				return PointF(x, y)
			}

			fun compute(frequency: Float, amplitude: Double): PointF
			{
				return compute(frequency, amplitude.toFloat())
			}
		}

		private var _isEnabled = enabled
		private var _isSelected = selected

		private val _filter = BiQuadraticFilter(
			type, frequency, gain, width, rate
		)
		private val _color: Int = color
		private val _path = Path()
		private val _points = mutableListOf<PointF>()
		private val _bubble = PointF()
		private val _amplitudes = mutableMapOf<Float, Double>()
		private lateinit var _backgroundShader: LinearGradient
		private lateinit var _shader: LinearGradient

		var isEnabled: Boolean
			set(value)
			{
				this._isEnabled = value
				this.setShaders()
			}
			get() = this._isEnabled

		var isSelected: Boolean
			set(value)
			{
				this._isSelected = value
				this.setShaders()
			}
			get() = this._isSelected
		
		val filter: BiQuadraticFilter
			get() = this._filter

		val backgroundColor: Int
			get() = when
			{
				this.isSelected -> this._color.toTransparent(0.4f)
				else -> this._color.toTransparent(0f)
			}

		val bubbleColor: Int
			get() = when
			{
				this.isEnabled -> this._color
				else -> this._color.toTransparent(0.5f)
			}

		val color: Int
			get() = when
			{
				this.isEnabled && this.isSelected -> this._color
				this.isEnabled && !this.isSelected -> this._color.toTransparent(0.75f)
				else -> this._color.toTransparent(0.5f)
			}
		
		val path: Path
			get() = this._path
		
		val points: List<PointF>
			get() = this._points.toList()

		val bubble: PointF
			get() = this._bubble
		
		val amplitudes: Map<Float, Double>
			get() = this._amplitudes.toMap()

		val backgroundShader: LinearGradient
			get() = this._backgroundShader

		val shader: LinearGradient
			get() = this._shader

		init
		{
			this.setShaders()
		}
			
		fun onDraw()
		{
			this._path.rewind()
			this._points.clear()
			this._amplitudes.clear()
			for(frequency in Limits.frequencies)
			{
				val amplitude = this._filter.amplitude(frequency.toDouble())
				val point = Band.compute(frequency, amplitude)
				this._points.add(point)
				this._amplitudes[frequency] = amplitude
			}
			this._path.fromPoints(this._points)
			val frequency = this._filter.frequency
			this._bubble.set(Band.compute(frequency.toFloat(), this._filter.amplitude(frequency)))
		}

		fun setShaders()
		{
			this._backgroundShader = Equalizer.shaderFactory(this.backgroundColor)
			this._shader = Equalizer.shaderFactory(this.color)
		}
	}

	private companion object
	{
		fun shaderFactory(color: Int): LinearGradient
		{
			val colorTransparent = Application.instance.applicationContext.getColor(android.R.color.transparent)
			return LinearGradient(
				0f,
				Bounds.heightTwoThirdWithPadding,
				0f,
				Bounds.height,
				color,
				colorTransparent,
				Shader.TileMode.CLAMP
			)
		}
	}
	
	private val _ticksXAxis = TicksXAxis()
	private val _ticksYAxis = TicksYAxis()
	private lateinit var _ticksXAxisPoints: List<Float>
	private lateinit var _ticksYAxisPoints: List<Float>
	private lateinit var _traceShader: LinearGradient
	private lateinit var _traceBackgroundShader: LinearGradient
	private val _gridLines = LineArray()
	private val _painter = Paint(Paint.ANTI_ALIAS_FLAG)
	private val _path = Path()
	private val _bands = mutableListOf<Band>()
	private val _rainbow = Rainbow()
	private var _primaryTraceColor = 0
	private var _primaryTraceStrokeWidth = 0f
	private var _secondaryTraceColor = 0
	private var _secondaryTraceStrokeWidth = 0f
	private var _gridColor = 0
	private var _gridStrokeWidth = 0f
	private var _labelXColor = 0
	private var _labelXSize = 0f
	private var _labelXPadding = 0f
	private var _labelYColor = 0
	private var _labelYSize = 0f
	private var _labelYPadding = 0f
	private var _bubbleSize = 0f
	
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
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
		{
			// Hardware acceleration disabled for prior API level 28, because:
			//  - Same type of shaders inside ComposeShader is unsupported
			// https://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
			this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
		}

		val theme = this.context.theme
		
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.Equalizer, defStyle, 0
		)
		
		// TODO change default values
		this._primaryTraceColor = typedArray.getColor(
			R.styleable.Equalizer_primaryTraceColor,
			this.resources.getColor(R.color.colorAccent, theme)
			// TODO move default values to another place
		)
		
		this._primaryTraceStrokeWidth = typedArray.getDimension(
			R.styleable.Equalizer_primaryTraceStrokeWidth,
			2f.fromDp()
		)
		
		this._secondaryTraceColor = typedArray.getColor(
			R.styleable.Equalizer_secondaryTraceColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this._secondaryTraceStrokeWidth = typedArray.getDimension(
			R.styleable.Equalizer_secondaryTraceStrokeWidth,
			1f.fromDp()
		)
		
		this._gridColor = typedArray.getColor(
			R.styleable.Equalizer_gridColor,
			this.resources.getColor(R.color.material_on_surface_disabled, theme)
		)
		
		this._gridStrokeWidth = typedArray.getDimension(
			R.styleable.Equalizer_gridStrokeWidth,
			1f.fromDp()
		)
		
		this._labelXColor = typedArray.getColor(
			R.styleable.Equalizer_labelXColor,
			this.resources.getColor(R.color.material_on_surface_disabled, theme)
		)
		
		this._labelXSize = typedArray.getDimension(
			R.styleable.Equalizer_labelXSize,
			16f.fromSp()
		)

		this._labelXPadding = typedArray.getDimension(
			R.styleable.Equalizer_labelXPadding,
			16f.fromDp()
		)
		
		this._labelYColor = typedArray.getColor(
			R.styleable.Equalizer_labelYColor,
			this.resources.getColor(R.color.material_on_surface_disabled, theme)
		)
		
		this._labelYSize = typedArray.getDimension(
			R.styleable.Equalizer_labelYSize,
			16f.fromSp()
		)

		this._labelYPadding = typedArray.getDimension(
			R.styleable.Equalizer_labelYPadding,
			16f.fromDp()
		)

		this._bubbleSize = typedArray.getDimension(
			R.styleable.Equalizer_bubbleSize,
			6f.fromDp()
		)
		
		typedArray.recycle()

		// TODO don"t forget to add these on get/set
		this._gridLines.color = this._gridColor
		this._gridLines.strokeWidth = this._gridStrokeWidth

		this._rainbow.shuffle()

		// TODO remove this
		// TODO handle case when bands is empty
		this._bands.add(
			Band(this._rainbow[0], true, true, BiQuadraticFilter.Type.LOW_PASS, 19000.0, 3.0, 1.2, 96000.0)
		)
		this._bands.add(
			Band(this._rainbow[1], true, false, BiQuadraticFilter.Type.LOW_SHELF, 30.0, -12.0, 0.707,  96000.0)
		)
		this._bands.add(
			Band(this._rainbow[2], true, false, BiQuadraticFilter.Type.BELL, 5000.0, 6.0, 1.2,  96000.0)
		)
		this._bands.add(
			Band(this._rainbow[3], true, false, BiQuadraticFilter.Type.BELL, 1500.0, 9.0, 4.0,  96000.0)
		)
		this._bands.add(
			Band(this._rainbow[4], true, false, BiQuadraticFilter.Type.NOTCH, 200.0, 3.0, 0.707,  96000.0)
		)
		this._bands.add(
			Band(this._rainbow[5], false, false, BiQuadraticFilter.Type.BAND_PASS, 1000.0, 3.0, 4.0,  96000.0)
		)
	}

	private fun setGridLinesShader()
	{
		val color = this._gridColor.toTransparent(0.7f)
		val colorTransparent = this.context.getColor(android.R.color.transparent)
		val topShader = LinearGradient(
			0f,
			Bounds.topWithPadding,
			0f,
			Bounds.top,
			color,
			colorTransparent,
			Shader.TileMode.CLAMP
		)
		val bottomShader = LinearGradient(
			0f,
			Bounds.heightTwoThirdWithPadding,
			0f,
			Bounds.height,
			color,
			colorTransparent,
			Shader.TileMode.CLAMP
		)
		val leftShader = LinearGradient(
			Bounds.leftWithPadding,
			0f,
			Bounds.left,
			0f,
			color,
			colorTransparent,
			Shader.TileMode.CLAMP
		)
		val rightShader = LinearGradient(
			Bounds.widthWithPadding,
			0f,
			Bounds.width,
			0f,
			color,
			colorTransparent,
			Shader.TileMode.CLAMP
		)
		val topBottomShader = ComposeShader(
			topShader, bottomShader, PorterDuff.Mode.MULTIPLY
		)
		val leftRightShader = ComposeShader(
			leftShader, rightShader, PorterDuff.Mode.MULTIPLY
		)
		val shader = ComposeShader(
			topBottomShader, leftRightShader, PorterDuff.Mode.MULTIPLY
		)
		this._gridLines.shader = shader
	}

	private fun setPrimaryTraceShader()
	{
		val color = this._primaryTraceColor
		val backgroundColor = this._primaryTraceColor
		this._traceShader = Equalizer.shaderFactory(color)
		this._traceBackgroundShader = Equalizer.shaderFactory(backgroundColor)
	}

	private fun setSecondaryTracesShader()
	{
		for(band in this._bands)
		{
			band.setShaders()
		}
	}

	override fun onSizeChanged(
		w: Int,
		h: Int,
		oldw: Int,
		oldh: Int
	)
	{
		super.onSizeChanged(w, h, oldw, oldh)
		Bounds.setBounds(this, 64f.fromDp(), 64f.fromDp(), 64f.fromDp(), 32f.fromDp())
		this.setGridLinesShader()
		this.setPrimaryTraceShader()
		this.setSecondaryTracesShader()
	}
	
	override fun onDraw(canvas: Canvas)
	{
		this._gridLines.clear()
		this._ticksXAxisPoints = this._ticksXAxis.points
		this._ticksYAxisPoints = this._ticksYAxis.points
		this.onDrawGrid(canvas)
		this.onDrawTraces(canvas)
		this.onDrawLabels(canvas)
	}
	
	private fun onDrawGrid(canvas: Canvas)
	{
		this.onDrawTicksX()
		this.onDrawTicksY()
		this._gridLines.onDraw(canvas, this._painter)
	}

	private fun onDrawTicksX()
	{
		val initial = this._ticksXAxisPoints.toMutableList()
		initial.removeFirst()
		initial.removeLast()
		val beforeSubset = this._ticksXAxisPoints.subList(0, 10)
		val before = beforeSubset
			.map { point -> point - (beforeSubset.last() - Bounds.leftWithPadding) }
			.filter { point -> point >= Bounds.left }
		val afterSubset = this._ticksXAxisPoints.subList(1, 10)
		val after = afterSubset
			.map { point -> point + (Bounds.widthWithPadding - afterSubset.first()) }
			.filter { point -> point <= Bounds.width }
		val points = listOf(initial, after, before).flatten()
		for(x in points)
		{
			this._gridLines.add(
				x,
				Bounds.top,
				x,
				Bounds.height
			)
		}
	}

	private fun onDrawTicksY()
	{
		// TODO pre calculate this
		val frame = Bounds.heightTwoThirdWithPadding - Bounds.topWithPadding
		val initial = this._ticksYAxisPoints.toMutableList()
		initial.removeFirst()
		initial.removeLast()
		val before = this._ticksYAxisPoints
			.map { point -> point - frame }
			.filter { point -> point >= Bounds.top }
		val after = this._ticksYAxisPoints
			.map { point -> point + frame }
			.filter { point -> point <= Bounds.height }
		val points = listOf(initial, after, before).flatten()
		for(y in points)
		{
			this._gridLines.add(
				Bounds.left,
				y,
				Bounds.width,
				y
			)
		}
	}
	
	private fun onDrawLabels(canvas: Canvas)
	{
		this.onDrawLabelsXAxis(canvas)
		this.onDrawLabelsYAxis(canvas)
	}
	
	private fun onDrawLabelsXAxis(canvas: Canvas)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._labelXColor
		this._painter.textSize = this._labelXSize
		this._painter.textAlign = Paint.Align.LEFT
		for(index in this._ticksXAxis.labels.indices)
		{
			val label = this._ticksXAxis.labels[index]
			val string = this._ticksXAxis.labelsString[index]
			val x = MathUtils.map(
				label,
				this._ticksXAxis.labelsMinimum,
				this._ticksXAxis.labelsMaximum,
				Bounds.leftWithPadding,
				Bounds.widthWithPadding
			)
			val y = Bounds.height - this._labelXPadding
			val point = this._painter.textAlignCenter(x, y, string)
			canvas.drawText(string, point.x, point.y, this._painter)
		}
	}
	
	private fun onDrawLabelsYAxis(canvas: Canvas)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._labelYColor
		this._painter.textSize = this._labelYSize
		this._painter.textAlign = Paint.Align.LEFT
		val lastIndex = this._ticksYAxis.ticks.size - 1
		for(index in this._ticksYAxis.ticks.indices)
		{
			val tick = this._ticksYAxis.ticks[index]
			val tickInt = -tick.toInt()
			val string = when {
				index == lastIndex -> {
					"$tickInt dB"
				}
				tickInt > 0 -> {
					"+$tickInt"
				}
				else -> {
					"$tickInt"
				}
			}
			val x = Bounds.width - this._labelYPadding
			val y = MathUtils.map(
				tick,
				this._ticksYAxis.ticksMinimum,
				this._ticksYAxis.ticksMaximum,
				Bounds.topWithPadding,
				Bounds.heightTwoThirdWithPadding
			)
			var point = this._painter.textAlignLeft(x, y, string)
			point = this._painter.textAlignVerticalCenter(point.x, point.y, string)
			canvas.drawText(string, point.x, point.y, this._painter)
		}
	}
	
	private fun onDrawTraces(canvas: Canvas)
	{
		this._painter.style = Paint.Style.STROKE
		this._painter.strokeWidth = this._secondaryTraceStrokeWidth
		for(band in this._bands)
		{
			band.onDraw()
			this._painter.color = band.color
			this._painter.shader = band.shader
			canvas.drawPath(band.path, this._painter)
			this._painter.shader = null
		}
		val selectedBand = this._bands.singleOrNull { band -> band.isEnabled && band.isSelected }
		this.onDrawPrimaryTrace(canvas, selectedBand != null)
		if(selectedBand != null)
		{
			this.onDrawSelectedTrace(canvas, selectedBand)
		}
		this.onDrawBubble(canvas)
	}

	private fun onDrawPrimaryTrace(canvas: Canvas, bandSelected: Boolean)
	{
		val points = this._bands
			// only select enabled bands
			.filter { x -> x.isEnabled }
			// flatten map of frequency -> amplitude
			.flatMap { x -> x.amplitudes.toList() }
			// group by frequency
			.groupBy { x -> x.first }
			// sum amplitudes and compute float points to grid points
			.map { x -> Band.compute(x.key, x.value.sumByDouble { it.second }) }
			.toMutableList()
		this._path.rewind()
		this._path.fromPoints(points)
		this._painter.style = Paint.Style.STROKE
		this._painter.color = this._primaryTraceColor
		this._painter.strokeWidth = this._primaryTraceStrokeWidth
		this._painter.shader = this._traceShader
		canvas.drawPath(this._path, this._painter)
		this._painter.shader = null
		// compute the two following lines even if a band is selected, that for path rewind efficiency
		this._path.lineToBaseline(points, Bounds.halfHeightTwoThirdWithPadding)
		this._path.close()
		if(bandSelected)
		{
			return
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._primaryTraceColor.toTransparent(0.09f)
		this._painter.shader = this._traceBackgroundShader
		canvas.drawPath(this._path, this._painter)
		this._painter.shader = null
	}

	private fun onDrawSelectedTrace(canvas: Canvas, band: Band)
	{
		when(band.filter.type)
		{
			BiQuadraticFilter.Type.BAND_PASS ->
			{
				band.path.close()
			}
			BiQuadraticFilter.Type.LOW_PASS ->
			{
				band.path.lineToMinimal(band.points)
			}
			BiQuadraticFilter.Type.HIGH_PASS ->
			{
				band.path.lineToMaximal(band.points)
			}
			else ->
			{
				band.path.lineToBaseline(band.points, Bounds.halfHeightTwoThirdWithPadding)
			}
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = band.backgroundColor
		this._painter.shader = band.backgroundShader
		canvas.drawPath(band.path, this._painter)
		this._painter.shader = null
	}

	private fun onDrawBubble(canvas: Canvas)
	{
		this._painter.strokeWidth = this._primaryTraceStrokeWidth
		for(band in this._bands)
		{
			this._painter.style = Paint.Style.FILL
			this._painter.color = band.bubbleColor
			when(band.filter.type)
			{
				BiQuadraticFilter.Type.LOW_PASS,
				BiQuadraticFilter.Type.HIGH_PASS,
				BiQuadraticFilter.Type.NOTCH ->
				run {
					val y = if(band.bubble.y.isNaN())
					{
						Bounds.height
					}
					else
					{
						band.bubble.y
					}
					this._painter.shader = band.shader
					canvas.drawLine(
						band.bubble.x,
						y,
						band.bubble.x,
						Bounds.halfHeightTwoThirdWithPadding,
						this._painter
					)
					this._painter.shader = null
					canvas.drawCircle(
						band.bubble.x,
						Bounds.halfHeightTwoThirdWithPadding,
						this._bubbleSize,
						this._painter
					)
					if(!band.isSelected)
					{
						return@run
					}
					this._painter.style = Paint.Style.STROKE
					this._painter.color = this._primaryTraceColor
					canvas.drawCircle(
						band.bubble.x,
						Bounds.halfHeightTwoThirdWithPadding,
						this._bubbleSize,
						this._painter
					)
				}
				else ->
				run {
					canvas.drawCircle(band.bubble.x, band.bubble.y, this._bubbleSize, this._painter)
					if(!band.isSelected)
					{
						return@run
					}
					this._painter.style = Paint.Style.STROKE
					this._painter.color = this._primaryTraceColor
					canvas.drawCircle(band.bubble.x, band.bubble.y, this._bubbleSize, this._painter)
				}
			}
		}
	}
}