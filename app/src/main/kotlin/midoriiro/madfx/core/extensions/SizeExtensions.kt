package midoriiro.madfx.core.extensions

import android.util.TypedValue
import midoriiro.madfx.core.Application

fun Float.toPixel(): Float
{
	val metrics = Application.instance.resources.displayMetrics
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
}

fun Float.toDp(): Float
{
	val metrics = Application.instance.resources.displayMetrics
	return this / metrics.density
}
