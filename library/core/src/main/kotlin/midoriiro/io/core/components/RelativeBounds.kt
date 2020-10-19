package midoriiro.io.core.components

import android.view.View
import midoriiro.io.core.components.Bounds

open class RelativeBounds : Bounds()
{
	override fun setBounds(
		view: View,
		paddingLeft: Float,
		paddingTop: Float,
		paddingRight: Float,
		paddingBottom: Float
	)
	{
		super.setBounds(view, paddingLeft, paddingTop, paddingRight, paddingBottom)
		val minX = 0f + view.paddingLeft
		val maxX = (view.width - view.paddingRight + view.paddingLeft).toFloat()
		val minY = 0f + view.paddingTop
		val maxY = (view.height - view.paddingBottom + view.paddingTop).toFloat()
		this.measure(minX, minY, maxX, maxY)
	}
}