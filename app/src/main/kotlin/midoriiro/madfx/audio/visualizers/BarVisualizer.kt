package midoriiro.madfx.audio.visualizers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import midoriiro.madfx.core.utils.MathUtils
import kotlin.math.abs
import kotlin.math.ceil


class BarVisualizer : BaseVisualizer
{
	private val _lines = mutableListOf<Float>()
	
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
		val barWidth = this.width / this.density
		val barCenter = barWidth / 2
		val step = this._samples!!.size / this.density
		val top = this.top.toFloat()
		val bottom = this.bottom.toFloat()
		this._painter.strokeWidth = barWidth - this.gap
		this._lines.clear()
		for (index in 0 until this.density.toInt())
		{
			val position = ceil(index * step).toInt()
			val sample = this.smooth(this._samples!!, position, this.smooth)
			val x = index * barWidth + barCenter
			val y = MathUtils.map(abs(sample.toFloat()),  Byte.MAX_VALUE.toFloat(), 0f, bottom, top)
			this._lines.add(x)
			this._lines.add(bottom)
			this._lines.add(x)
			this._lines.add(y)
		}
		canvas.drawLines(this._lines.toFloatArray(), this._painter)
	}
}