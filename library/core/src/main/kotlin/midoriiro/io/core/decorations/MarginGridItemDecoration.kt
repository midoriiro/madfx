package midoriiro.io.core.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarginGridItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		if(parent.layoutManager !is GridLayoutManager)
		{
			return
		}
		
		val manager = parent.layoutManager as GridLayoutManager
		val spanCount = manager.spanCount
		val position = parent.getChildAdapterPosition(view)
		val column = position % spanCount
		
		outRect.left = this.space - column * this.space / spanCount
		outRect.right = (column + 1) * this.space / spanCount
		
		if(position < spanCount)
		{
			outRect.top = this.space
		}
		
		outRect.bottom = this.space
	}
}