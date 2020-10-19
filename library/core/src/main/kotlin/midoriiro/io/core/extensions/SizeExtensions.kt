package midoriiro.io.core.extensions

import android.util.TypedValue
import midoriiro.io.core.Application

fun Float.toPixel(): Float
{
	val metrics = Application.instance.resources.displayMetrics
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
}

fun Float.fromDp(): Float
{
	val metrics = Application.instance.resources.displayMetrics
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
}

fun Float.fromSp(): Float
{
	val metrics = Application.instance.resources.displayMetrics
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, metrics)
}
