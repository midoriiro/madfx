package midoriiro.madfx.core.components

import android.view.View

open class AbsoluteBounds : Bounds()
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
		val minX = (view.left + view.paddingLeft).toFloat()
		val maxX = (view.right - view.paddingRight).toFloat()
		val minY = (view.top + view.paddingTop).toFloat()
		val maxY = (view.bottom - view.paddingBottom).toFloat()
		this.measure(minX, minY, maxX, maxY)
	}
}