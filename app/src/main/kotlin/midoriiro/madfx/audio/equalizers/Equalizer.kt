package midoriiro.madfx.audio.equalizers

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import midoriiro.io.core.Application.Singleton.instance
import midoriiro.madfx.R
import midoriiro.madfx.audio.equalizers.adapters.BandFilterAdapter
import midoriiro.madfx.audio.equalizers.filters.BiQuadraticFilter
import midoriiro.madfx.core.Application
import midoriiro.io.core.collections.LineArray
import midoriiro.io.core.colors.Rainbow
import midoriiro.io.core.components.RelativeBounds
import midoriiro.io.core.extensions.*
import midoriiro.io.core.utils.MathUtils
import kotlin.math.log10
import kotlin.math.pow

class Equalizer : View
{
	private class Bounds : RelativeBounds()
	{
		private var _heightTwoThird = 0f
		private var _heightTwoThirdWithPadding = 0f
		private var _halfHeightTwoThird = 0f
		private var _halfHeightTwoThirdWithPadding = 0f

		val heightTwoThird: Float
			get() = this._heightTwoThird

		val heightTwoThirdWithPadding: Float
			get() = this._heightTwoThirdWithPadding

		val halfHeightTwoThird: Float
			get() = this._halfHeightTwoThird

		val halfHeightTwoThirdWithPadding: Float
			get() = this._halfHeightTwoThirdWithPadding

		override fun setBounds(
			view: View,
			paddingLeft: Float,
			paddingTop: Float,
			paddingRight: Float,
			paddingBottom: Float
		)
		{
			super.setBounds(view, paddingLeft, paddingTop, paddingRight, paddingBottom)
			this._heightTwoThird = this.height / 3f * 2f
			this._heightTwoThirdWithPadding = this.heightWithPadding / 3f * 2f
			this._halfHeightTwoThird = this._heightTwoThird / 2f + paddingTop / 2f
			this._halfHeightTwoThirdWithPadding = this._heightTwoThirdWithPadding / 2f + this.topWithPadding / 2f
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
						if(maximum > 50000f)
						{
							maximum = 50000f
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
	
	private inner class TicksXAxis
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
						_bounds.leftWithPadding,
						_bounds.widthWithPadding
					)
					list.add(x)
				}
				return list.toList()
			}
	}
	
	private inner class TicksYAxis
	{
		private val _ticks = listOf(
			12f, 9f, 6f, 3f,
			0f,
			-3f, -6f, -9f, -12f,
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
						_bounds.topWithPadding,
						_bounds.heightTwoThirdWithPadding
					)
					list.add(y)
				}
				return list.toList()
			}
	}
	
	private class Band(
		bounds: Bounds,
		color: Int,
		enabled: Boolean,
		selected: Boolean,
		type: BiQuadraticFilter.Type,
		frequency: Double,
		gain: Double,
		width: Double
	)
	{
		companion object
		{
			fun compute(frequency: Float, amplitude: Float, bounds: Bounds): PointF
			{
				val x = MathUtils.map(
					log10(frequency),
					Limits.frequencyLog10Minimum,
					Limits.frequencyLog10Maximum,
					bounds.leftWithPadding,
					bounds.widthWithPadding
				)
				val y = MathUtils.map(
					-amplitude,
					Limits.gainFloatMinimum,
					Limits.gainFloatMaximum,
					bounds.topWithPadding,
					bounds.heightTwoThirdWithPadding
				)
				return PointF(x, y)
			}

			fun compute(frequency: Float, amplitude: Double, bounds: Bounds): PointF
			{
				return compute(frequency, amplitude.toFloat(), bounds)
			}
		}

		private val _filter = BiQuadraticFilter(
			type, frequency, gain, width, 96000.0
		)
		private var _isEnabled = enabled
		private var _isSelected = selected
		private val _bounds = bounds
		private val _color = color
		private val _backgroundPath = Path()
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

		var type: BiQuadraticFilter.Type
			set(value)
			{
				this._filter.type = value
			}
			get() = this._filter.type

		var frequency: Double
			set(value)
			{
				this._filter.frequency = value
			}
			get() = this._filter.frequency

		var gain: Double
			set(value)
			{
				this._filter.gain = value
			}
			get() = this._filter.gain

		var width: Double
			set(value)
			{
				this._filter.width = value
			}
			get() = this._filter.width

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

		val backgroundPath: Path
			get() = this._backgroundPath
		
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
			this._backgroundPath.rewind()
			this._path.rewind()
			this._points.clear()
			this._amplitudes.clear()
			for(frequency in Limits.frequencies)
			{
				val amplitude = this._filter.amplitude(frequency.toDouble())
				val point = Band.compute(frequency, amplitude, this._bounds)
				this._points.add(point)
				this._amplitudes[frequency] = amplitude
			}
			val frequency = this._filter.frequency
			this._bubble.set(Band.compute(frequency.toFloat(), this._filter.amplitude(frequency), this._bounds))
			this._path.fromPoints(this._points)
			this._backgroundPath.fromPoints(this._points)
			when(this.type)
			{
				BiQuadraticFilter.Type.BAND_PASS ->
				{
					this._backgroundPath.close()
				}
				BiQuadraticFilter.Type.LOW_PASS ->
				{
					this._backgroundPath.lineToMinimal(this._points)
				}
				BiQuadraticFilter.Type.HIGH_PASS ->
				{
					this._backgroundPath.lineToMaximal(this._points)
				}
				else ->
				{
					this._backgroundPath.lineToBaseline(this._points, this._bounds.halfHeightTwoThirdWithPadding)
				}
			}
		}

		fun setShaders()
		{
			this._backgroundShader = Equalizer.shaderFactory(this.backgroundColor, this._bounds)
			this._shader = Equalizer.shaderFactory(this.color, this._bounds)
		}
	}

	private inner class Listener : BandFilterAdapter.Listener()
	{
		override fun onItemInserted(index: Int)
		{
			val band = _adapter.get(index)
			_bands.add(Band(
				_bounds,
				_rainbow[index],
				band.isEnabled,
				band.isSelected,
				band.type,
				band.frequency,
				band.gain,
				band.width
			))
			invalidate()
		}

		override fun onItemRemoved(index: Int)
		{
			_bands.removeAt(index)
			invalidate()
		}

		override fun onItemTypeChanged(
			index: Int,
			type: BiQuadraticFilter.Type
		)
		{
			_bands[index].type = type
			invalidate()
		}

		override fun onItemFrequencyChanged(
			index: Int,
			frequency: Double
		)
		{
			_bands[index].frequency = frequency
			invalidate()
		}

		override fun onItemGainChanged(
			index: Int,
			gain: Double
		)
		{
			_bands[index].gain = gain
			invalidate()
		}

		override fun onItemWidthChanged(
			index: Int,
			width: Double
		)
		{
			_bands[index].width = width
			invalidate()
		}

		override fun onItemEnabledChanged(
			index: Int,
			state: Boolean
		)
		{
			_bands[index].isEnabled = state
			invalidate()
		}

		override fun onItemSelectionChanged()
		{
			for(index in 0 until _adapter.size())
			{
				val band = _adapter.get(index)
				_bands[index].isSelected = band.isSelected
			}
			invalidate()
		}
	}

	private companion object
	{
		fun shaderFactory(color: Int, bounds: Bounds): LinearGradient
		{
			val colorTransparent = Application.instance.applicationContext.getColor(android.R.color.transparent)
			return LinearGradient(
				0f,
				bounds.heightTwoThirdWithPadding,
				0f,
				bounds.height,
				color,
				colorTransparent,
				Shader.TileMode.CLAMP
			)
		}
	}

	private val _adapter = BandFilterAdapter()
	private val _bounds = Bounds()
	private val _ticksXAxis = TicksXAxis()
	private val _ticksYAxis = TicksYAxis()
	private lateinit var _ticksXAxisPoints: List<Float>
	private lateinit var _ticksYAxisPoints: List<Float>
	private lateinit var _traceShader: LinearGradient
	private lateinit var _traceBackgroundShader: LinearGradient
	private val _painter = Paint(Paint.ANTI_ALIAS_FLAG)
	private val _gridLines = LineArray()
	private val _primaryTrace = Path()
	private val _bands = mutableListOf<Band>()
	private val _rainbow = Rainbow()
	private var _primaryTraceColor = 0
	private var _primaryTraceStrokeWidth = 0f
	private var _secondaryTraceColor = 0
	private var _secondaryTraceStrokeWidth = 0f
	private var _gridColor = 0
	private var _gridStrokeWidth = 0f
	private var _gridPaddingTop = 0f
	private var _gridPaddingBottom = 0f
	private var _gridPaddingLeft = 0f
	private var _gridPaddingRight = 0f
	private var _labelXColor = 0
	private var _labelXSize = 0f
	private var _labelXPadding = 0f
	private var _labelYColor = 0
	private var _labelYSize = 0f
	private var _labelYPadding = 0f
	private var _bubbleSize = 0f

	val adapter: BandFilterAdapter
		get() = this._adapter
	
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
		// Hardware acceleration disabled for prior API level 28, because:
		//  - Same type of shaders inside ComposeShader is unsupported
		// https://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

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

		this._gridPaddingTop = typedArray.getDimension(
			R.styleable.Equalizer_gridPaddingTop,
			32f
		)

		this._gridPaddingBottom = typedArray.getDimension(
			R.styleable.Equalizer_gridPaddingBottom,
			32f
		)

		this._gridPaddingLeft = typedArray.getDimension(
			R.styleable.Equalizer_gridPaddingLeft,
			32f
		)

		this._gridPaddingRight = typedArray.getDimension(
			R.styleable.Equalizer_gridPaddingRight,
			128f
		)
		
		this._labelXColor = typedArray.getColor(
			R.styleable.Equalizer_labelXColor,
			this.resources.getColor(R.color.material_on_surface_disabled, theme)
		)
		
		this._labelXSize = typedArray.getDimension(
			R.styleable.Equalizer_labelXSize,
			20f
		)

		this._labelXPadding = typedArray.getDimension(
			R.styleable.Equalizer_labelXPadding,
			10f
		)
		
		this._labelYColor = typedArray.getColor(
			R.styleable.Equalizer_labelYColor,
			this.resources.getColor(R.color.material_on_surface_disabled, theme)
		)
		
		this._labelYSize = typedArray.getDimension(
			R.styleable.Equalizer_labelYSize,
			20f
		)

		this._labelYPadding = typedArray.getDimension(
			R.styleable.Equalizer_labelYPadding,
			10f
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

		this._adapter.addListener(Listener())

		// TODO remove this
		// TODO handle case when bands is empty
		this._bands.add(
			Band(this._bounds, this._rainbow[0], true, false, BiQuadraticFilter.Type.LOW_PASS, 19000.0, 3.0, 1.2)
		)
		this._bands.add(
			Band(this._bounds, this._rainbow[1], true, false, BiQuadraticFilter.Type.LOW_SHELF, 30.0, -12.0, 0.707)
		)
		this._bands.add(
			Band(this._bounds, this._rainbow[2], true, false, BiQuadraticFilter.Type.BELL, 5000.0, 6.0, 1.2)
		)
		this._bands.add(
			Band(this._bounds, this._rainbow[3], true, false, BiQuadraticFilter.Type.BELL, 1500.0, 9.0, 4.0)
		)
		this._bands.add(
			Band(this._bounds, this._rainbow[4], true, false, BiQuadraticFilter.Type.NOTCH, 200.0, 3.0, 0.707)
		)
		this._bands.add(
			Band(this._bounds, this._rainbow[5], false, false, BiQuadraticFilter.Type.BAND_PASS, 1000.0, 3.0, 4.0)
		)
	}

	private fun setGridLinesShader()
	{
		val color = this._gridColor.toTransparent(0.7f)
		val colorTransparent = this.context.getColor(android.R.color.transparent)
		val topShader = LinearGradient(
			0f,
			this._bounds.topWithPadding,
			0f,
			this._bounds.top,
			color,
			colorTransparent,
			Shader.TileMode.CLAMP
		)
		val bottomShader = LinearGradient(
			0f,
			this._bounds.heightTwoThirdWithPadding,
			0f,
			this._bounds.height,
			color,
			colorTransparent,
			Shader.TileMode.CLAMP
		)
		val leftShader = LinearGradient(
			this._bounds.leftWithPadding,
			0f,
			this._bounds.left,
			0f,
			color, colorTransparent,
			Shader.TileMode.CLAMP
		)
		val rightShader = LinearGradient(
			this._bounds.widthWithPadding,
			0f,
			this._bounds.width,
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
		this._traceShader = Equalizer.shaderFactory(color, this._bounds)
		this._traceBackgroundShader = Equalizer.shaderFactory(backgroundColor, this._bounds)
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
		this._bounds.setBounds(
			this,
			this._gridPaddingLeft,
			this._gridPaddingTop,
			this._gridPaddingRight,
			this._gridPaddingBottom
		)
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
		val beforeSubset = this._ticksXAxisPoints.subList(0, 10) // select 10Hz -> 100Hz
		val before = beforeSubset
			.map { point -> point - (beforeSubset.last() - this._bounds.leftWithPadding) }
			.filter { point -> point >= this._bounds.left }
		val afterSubset = this._ticksXAxisPoints.subList(1, 10) // select 20Hz -> 100Hz
		val after = afterSubset
			.map { point -> point + (this._bounds.widthWithPadding - afterSubset.first()) }
			.filter { point -> point <= this._bounds.width }
		val points = listOf(initial, after, before).flatten()
		for(x in points)
		{
			this._gridLines.add(
				x,
				this._bounds.top,
				x,
				this._bounds.height
			)
		}
	}

	private fun onDrawTicksY()
	{
		// TODO pre calculate this
		val frame = this._bounds.heightTwoThirdWithPadding - this._bounds.topWithPadding
		val initial = this._ticksYAxisPoints.toMutableList()
		initial.removeFirst()
		initial.removeLast()
		val before = this._ticksYAxisPoints
			.map { point -> point - frame }
			.filter { point -> point >= this._bounds.top }
		val after = this._ticksYAxisPoints
			.map { point -> point + frame }
			.filter { point -> point <= this._bounds.height }
		val points = listOf(initial, after, before).flatten()
		for(y in points)
		{
			this._gridLines.add(
				this._bounds.left,
				y,
				this._bounds.width,
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
				this._bounds.leftWithPadding,
				this._bounds.widthWithPadding
			)
			val y = this._bounds.height - this._labelXPadding
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
			val x = this._bounds.width - this._labelYPadding
			val y = MathUtils.map(
				tick,
				this._ticksYAxis.ticksMinimum,
				this._ticksYAxis.ticksMaximum,
				this._bounds.topWithPadding,
				this._bounds.heightTwoThirdWithPadding
			)
			var point = this._painter.textAlignLeft(x, y, string)
			point = this._painter.textAlignVerticalCenter(point.x, point.y, string)
			canvas.drawText(string, point.x, point.y, this._painter)
		}
	}
	
	private fun onDrawTraces(canvas: Canvas)
	{
		if(this._bands.isEmpty())
		{
			return
		}
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
			.map { x -> Band.compute(x.key, x.value.sumByDouble { it.second }, this._bounds) }
			.toMutableList()
		this._primaryTrace.rewind()
		this._primaryTrace.fromPoints(points)
		this._painter.style = Paint.Style.STROKE
		this._painter.color = this._primaryTraceColor
		this._painter.strokeWidth = this._primaryTraceStrokeWidth
		this._painter.shader = this._traceShader
		canvas.drawPath(this._primaryTrace, this._painter)
		this._painter.shader = null
		// compute the two following lines even if a band is selected, that for path rewind efficiency
		this._primaryTrace.lineToBaseline(points, this._bounds.halfHeightTwoThirdWithPadding)
		this._primaryTrace.close()
		if(bandSelected)
		{
			return
		}
		this._painter.style = Paint.Style.FILL
		this._painter.color = this._primaryTraceColor.toTransparent(0.09f)
		this._painter.shader = this._traceBackgroundShader
		canvas.drawPath(this._primaryTrace, this._painter)
		this._painter.shader = null
	}

	private fun onDrawSelectedTrace(canvas: Canvas, band: Band)
	{
		this._painter.style = Paint.Style.FILL
		this._painter.color = band.backgroundColor
		this._painter.shader = band.backgroundShader
		canvas.drawPath(band.backgroundPath, this._painter)
		this._painter.shader = null
	}

	private fun onDrawBubble(canvas: Canvas)
	{
		this._painter.strokeWidth = this._primaryTraceStrokeWidth
		for(band in this._bands)
		{
			this._painter.style = Paint.Style.FILL
			this._painter.color = band.bubbleColor
			when(band.type)
			{
				BiQuadraticFilter.Type.LOW_PASS,
				BiQuadraticFilter.Type.HIGH_PASS,
				BiQuadraticFilter.Type.NOTCH ->
				run {
					val y = if(band.bubble.y.isNaN())
					{
						this._bounds.height
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
						this._bounds.halfHeightTwoThirdWithPadding,
						this._painter
					)
					this._painter.shader = null
					canvas.drawCircle(
						band.bubble.x,
						this._bounds.halfHeightTwoThirdWithPadding,
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
						this._bounds.halfHeightTwoThirdWithPadding,
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