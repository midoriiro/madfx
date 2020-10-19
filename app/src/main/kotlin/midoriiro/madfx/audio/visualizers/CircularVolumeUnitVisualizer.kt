package midoriiro.madfx.audio.visualizers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import midoriiro.io.core.utils.MathUtils
import kotlin.math.abs
import kotlin.math.min

class CircularVolumeUnitVisualizer : BaseVisualizer
{
	constructor(
		context: Context,
		attrs: AttributeSet
	) : super(context, attrs)
	{
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet,
		defStyleAttr: Int,
		defStyleRes: Int
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
		val x = this.width.toFloat() / 2f
		val y = this.height.toFloat() / 2f
		val edge = min(x, y)
		val sample = this.rms(this._samples!!)
		val radius = MathUtils.map(abs(sample),  Byte.MAX_VALUE.toFloat(), 0f,  this.radius, edge)
		canvas.drawCircle(x, y, radius, this._painter)
	}
}