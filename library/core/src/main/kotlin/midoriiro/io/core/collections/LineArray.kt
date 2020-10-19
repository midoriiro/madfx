package midoriiro.io.core.collections

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader

class LineArray
{
    private var _color = 0
    private var _shader: Shader? = null
    private var _strokeWidth = 0f
    private val _lines = mutableListOf<Float>()

    var color: Int
        set(value) {
            this._color = value
        }
        get() = this._color

    var shader: Shader?
        set(value) {
            this._shader = value
        }
        get() = this._shader

    var strokeWidth: Float
        set(value) {
            this._strokeWidth = value
        }
        get() = this._strokeWidth

    fun add(xStart: Float, yStart: Float, xEnd: Float, yEnd: Float)
    {
        this._lines.add(xStart)
        this._lines.add(yStart)
        this._lines.add(xEnd)
        this._lines.add(yEnd)
    }

    fun clear()
    {
        this._lines.clear()
    }

    fun onDraw(canvas: Canvas, painter: Paint)
    {
        painter.style = Paint.Style.STROKE
        if(this._shader == null)
        {
            painter.color = this._color
        }
        else
        {
            painter.shader = this._shader
        }
        painter.strokeWidth = this.strokeWidth
        canvas.drawLines(
            this._lines.toFloatArray(),
            painter
        )
        painter.shader = null
    }
}