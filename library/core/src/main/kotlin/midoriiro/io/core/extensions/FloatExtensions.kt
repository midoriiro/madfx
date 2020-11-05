package midoriiro.io.core.extensions

import kotlin.math.PI

fun Float.toRadians(): Float
{
	return this / 180.0f * PI.toFloat()
}

fun Float.toDegrees(): Float
{
	return this * 180.0f / PI.toFloat()
}

fun Float.toClockwise(): Float
{
	return 360f - this
}

fun Float.clamp(minimum: Float, maximum: Float): Float
{
	return when
	{
		this < minimum -> minimum
		this > maximum -> maximum
		else -> this
	}
}

fun Float.sign(minimum: Float, maximum: Float, pivot: Float): Float
{
	val middle = minimum + pivot
	return when (this)
	{
		in minimum..middle -> -1f
		in middle..maximum -> 1f
		else -> 0f
	}
}