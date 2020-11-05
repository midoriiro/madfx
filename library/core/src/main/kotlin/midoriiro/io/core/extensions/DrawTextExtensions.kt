package midoriiro.io.core.extensions

import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF

private val bounds = Rect()

/**
 * Text align to center, which mean from text bound reference point (bottom left corner)
 */
fun Paint.textAlignCenter(x: Float, y: Float, text: String): PointF
{
	this.getTextBounds(text, 0, text.length, bounds)
	return PointF(x - bounds.exactCenterX(), y)
}

/**
 * Text align to vertical center, which mean from text bound reference point (bottom left corner)
 */
fun Paint.textAlignVerticalCenter(x: Float, y: Float, text: String): PointF
{
	this.getTextBounds(text, 0, text.length, bounds)
	return PointF(x, y - bounds.exactCenterY())
}

/**
 * Text align to left, which mean from text bound reference point (bottom left corner)
 */
fun Paint.textAlignLeft(x: Float, y: Float, text: String): PointF
{
	this.getTextBounds(text, 0, text.length, bounds)
	return PointF(x - bounds.width(), y)
}

/**
 * Text align to right, which mean from text bound reference point (bottom left corner)
 */
fun Paint.textAlignRight(x: Float, y: Float, text: String): PointF
{
	return PointF(x, y)
}

fun Paint.textBounds(text: String): RectF
{
	this.getTextBounds(text, 0, text.length, bounds)
	return RectF(
		bounds.left.toFloat(),
		bounds.top.toFloat(),
		bounds.right.toFloat(),
		bounds.bottom.toFloat()
	)
}