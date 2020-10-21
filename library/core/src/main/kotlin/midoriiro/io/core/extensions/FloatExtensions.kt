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