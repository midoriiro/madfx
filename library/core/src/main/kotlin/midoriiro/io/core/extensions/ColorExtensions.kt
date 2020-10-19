package midoriiro.io.core.extensions

import androidx.core.graphics.ColorUtils
import midoriiro.io.core.utils.MathUtils

fun Int.toTransparent(alpha: Float): Int
{
	if(alpha < 0 || 1 < alpha)
	{
		throw IllegalArgumentException("alpha must be between 0 and 1")
	}
	return ColorUtils.setAlphaComponent(
		this,
		MathUtils
			.map(alpha, 0f, 1f, 0f, 255f).toInt()
	)
}

fun Int.toHighEmphasis(): Int
{
	return this.toTransparent(0.87f)
}

fun Int.toMediumEmphasis(): Int
{
	return this.toTransparent(0.60f)
}

fun Int.toLowEmphasis(): Int
{
	return this.toTransparent(0.38f)
}

fun Int.toDisabledState(): Int
{
	return this.toTransparent(0.12f)
}

fun Int.toOverlayEnabled(): Int
{
	return this.toTransparent(0f)
}

fun Int.toOverlayHovered(): Int
{
	return this.toTransparent(0.04f)
}

fun Int.toOverlayFocused(): Int
{
	return this.toTransparent(0.12f)
}

fun Int.toOverlayPressed(): Int
{
	return this.toTransparent(0.10f)
}

fun Int.toOverlayDragged(): Int
{
	return this.toTransparent(0.08f)
}

fun Int.toStrokeFocused(): Int
{
	return this.toTransparent(1f)
}
