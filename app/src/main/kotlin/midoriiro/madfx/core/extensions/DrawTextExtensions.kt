package midoriiro.madfx.core.extensions

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect

private val bounds = Rect()

/**
 * Text align to center, which mean from text bound reference point (bottom left corner)
 */
fun Paint.textAlignCenter(x: Float, y: Float, text: String): PointF
{
	this.getTextBounds(text, 0, text.length, bounds)
	return PointF(x - bounds.width() / 2f, y)
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