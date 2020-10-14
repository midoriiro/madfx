package midoriiro.madfx.core.utils

import android.graphics.PointF
import midoriiro.madfx.core.extensions.toRadians
import kotlin.math.cos
import kotlin.math.sin

class MathUtils
{
	companion object
	{
		fun map(value: Float, a: Float, b: Float, c: Float, d: Float): Float
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
		fun map(value: Double, a: Double, b: Double, c: Double, d: Double): Double
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
		fun map(value: Int, a: Int, b: Int, c: Int, d: Int): Int
		{
			return (value - a) / (b - a) * (d-c) + c
		}

		fun rotate(px: Float, py: Float, cx: Float, cy: Float, angle: Float): PointF
		{
			val a = angle.toRadians()
			val cos = cos(a)
			val sin = sin(a)
			val x1 = px - cx
			val y1 = py - cy
			val x2 = x1 * cos - y1 * sin
			val y2 = x1 * sin + y1 * cos
			return PointF(x2 + cx, y2 + cy)
		}
	}
}