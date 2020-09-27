package midoriiro.madfx.core.extensions

import androidx.core.graphics.ColorUtils
import midoriiro.madfx.core.utils.MathUtils

fun Int.toTransparent(alpha: Float): Int
{
	if(alpha < 0 || 1 < alpha)
	{
		throw IllegalArgumentException("alpha must be between 0 and 1")
	}
	return ColorUtils.setAlphaComponent(
		this,
		MathUtils.map(alpha, 0f, 1f, 0f, 255f).toInt()
	)
}
