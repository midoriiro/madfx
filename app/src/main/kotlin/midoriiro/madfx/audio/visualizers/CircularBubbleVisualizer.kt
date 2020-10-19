package midoriiro.madfx.audio.visualizers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import midoriiro.io.core.extensions.toRadians
import midoriiro.io.core.utils.MathUtils
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class CircularBubbleVisualizer : BaseVisualizer
{
	private val _path = Path()
	private val _points = MutableList(this.MAXIMUM_DENSITY.toInt()){PointF()}
	
	private var _density: Float = 0f
	private var _radius: Float = 0f
	
	
	constructor(
		context: Context, attrs: AttributeSet
	) : super(context, attrs)
	{
	}
	
	constructor(
		context: Context, attrs: AttributeSet, defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
	}
	
	constructor(
		context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
	}
	
	override fun init(attrs: AttributeSet?, defStyle: Int)
	{
		this._painter.style = Paint.Style.FILL
	}
	
	override fun onDraw(canvas: Canvas)
	{
		if(this._samples == null)
		{
			return
		}
		val densityStep = this._samples!!.size / this.density
		val circularStep = 360f / this.density
		val centerX = this.width.toFloat() / 2f
		val centerY = this.height.toFloat() / 2f
		val edge = min(centerX, centerY)
		this._path.reset()
		for (index in 0 until this._density.toInt())
		{
			val position = ceil(index * densityStep).toInt()
			val angle = circularStep * index
			val sample = this._samples!![position]
			val radius = MathUtils.map(abs(sample.toFloat()),  Byte.MAX_VALUE.toFloat(), 0f,  this._radius, edge)
			val x = centerX + cos(angle.toRadians()) * radius
			val y = centerY + sin(angle.toRadians()) * radius
			this._points[index].set(x, y)
			if(index == 0)
			{
				this._path.moveTo(x, y)
			}
			else
			{
				val point = this._points[index - 1]
				this._path.quadTo(point.x, point.y, x, y)
			}
		}
		this._path.close()
		canvas.drawPath(this._path, this._painter)
	}
}