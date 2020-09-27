package midoriiro.madfx.core.extensions

import kotlin.math.PI

fun Float.toRadians(): Float
{
	return this / 180.0f * PI.toFloat()
}