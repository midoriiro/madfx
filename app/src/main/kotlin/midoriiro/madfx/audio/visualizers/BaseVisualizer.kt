package midoriiro.madfx.audio.visualizers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import midoriiro.madfx.R
import midoriiro.madfx.audio.events.WaveFormDataCaptureEvent
import midoriiro.madfx.audio.services.VisualizerService
import midoriiro.madfx.core.extensions.toTransparent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.sqrt

abstract class BaseVisualizer : View
{
	protected val MINIMUM_RADIUS = 0f
	protected val MINIMUM_SMOOTH = 0
	protected val MINIMUM_GAP = 0
	protected val MINIMUM_DENSITY = 3f
	protected val MAXIMUM_DENSITY = 1024f
	
	protected val _visualizer = VisualizerService.visualizer
	protected val _painter = Paint(Paint.ANTI_ALIAS_FLAG)
	protected var _samples: ByteArray? = null
	
	private var _normalize: Boolean = false
	private var _color: Int = 0
	private var _opacity: Float = 1f
	private var _density: Float = 0f
	private var _gap: Int = 0
	private var _smooth: Int = 0
	private var _radius: Float = 0f
	
	var normalize: Boolean
		set(value)
		{
			this._normalize = value
			VisualizerService.isNormalized = value
		}
		get() = this._normalize
	
	var color: Int
		set(value)
		{
			this._color = value
			this._painter.color = this._color.toTransparent(this._opacity)
			this.invalidate()
		}
		get() = this._color
	
	var opacity: Float
		set(value)
		{
			this._opacity = value
			this._color = this._color.toTransparent(this._opacity)
			this.invalidate()
		}
		get() = this._opacity
	
	var density: Float
		set(value)
		{
			this._density = value
			
			if(this._density < this.MINIMUM_DENSITY)
			{
				this._density = this.MINIMUM_DENSITY
			}
			else if(this._density > this.MAXIMUM_DENSITY)
			{
				this._density = this.MAXIMUM_DENSITY
			}
			
			this.invalidate()
		}
		get() = this._density
	
	var gap: Int
		set(value)
		{
			this._gap = value
			
			if(this._gap < this.MINIMUM_GAP)
			{
				this._gap = this.MINIMUM_GAP
			}
			
			this.invalidate()
		}
		get() = this._gap
	
	var smooth: Int
		set(value)
		{
			this._smooth = value
			
			if(this._smooth < this.MINIMUM_SMOOTH)
			{
				this._smooth = this.MINIMUM_SMOOTH
			}
			else if(this._smooth > this.density)
			{
				this._smooth = this._density.toInt()
			}
		}
		get() = this._smooth
	
	var radius: Float
		set(value)
		{
			this._radius = value
			
			if(this._radius < this.MINIMUM_RADIUS)
			{
				this._radius = this.MINIMUM_RADIUS
			}
			
			this.invalidate()
		}
		get() = this._radius
	
	constructor(
		context: Context,
		attrs: AttributeSet
	) : super(context, attrs)
	{
		this.baseInit(attrs, 0)
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.baseInit(attrs, defStyleAttr)
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.baseInit(attrs, defStyleAttr)
	}
	
	@SuppressLint("CustomViewStyleable")
	private fun baseInit(attrs: AttributeSet, defStyle: Int)
	{
		val theme = this.context.theme
		
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.Visualizer, defStyle, 0
		)
		
		this._normalize = typedArray.getBoolean(
			R.styleable.Visualizer_normalize,
			false
		)
		
		this._opacity = typedArray.getFloat(
			R.styleable.Visualizer_opacity,
			1f
		)
		
		this.color = typedArray.getColor(
			R.styleable.Visualizer_color,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.density = typedArray.getFloat(
			R.styleable.Visualizer_density, 50f
		)
		
		this.gap = typedArray.getInt(
			R.styleable.Visualizer_gap, 4
		)
		
		this._smooth = typedArray.getInt(
			R.styleable.Visualizer_smooth, 2
		)
		
		this.radius = typedArray.getDimension(
			R.styleable.Visualizer_radius, 0f
		)
		
		typedArray.recycle()
		
		this.init(attrs, defStyle)
	}
	
	override fun onAttachedToWindow()
	{
		super.onAttachedToWindow()
		EventBus.getDefault().register(this)
	}
	
	override fun onDetachedFromWindow()
	{
		super.onDetachedFromWindow()
		EventBus.getDefault().unregister(this)
	}
	
	protected abstract fun init(attrs: AttributeSet?, defStyle: Int)
	
	@Subscribe
	fun onWaveFormDataCapture(event: WaveFormDataCaptureEvent)
	{
		if(!this.isEnabled)
		{
			return
		}
		this._samples = event.waveform
		this.invalidate()
	}
	
	protected fun smooth(samples: ByteArray, position: Int, bandwidth: Int): Byte
	{
		if(bandwidth <= 0)
		{
			return samples[position]
		}
		
		var left = position - bandwidth
		var right = position + bandwidth
		
		if(left < 0)
		{
			left = 0
		}
		
		if(right > samples.size - 1)
		{
			right = samples.size - 1
		}
		
		return samples
			.asList()
			.subList(left, right + 1)
			.average()
			.toInt()
			.toByte()
	}
	
	protected fun rms(samples: ByteArray): Float
	{
		val sum = samples
			.map { sample ->
				sample
			}
			.sumBy { sample ->
				sample * sample
			}
		val mean = sum / samples.size
		return sqrt(mean.toFloat())
	}
}