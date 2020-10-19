package midoriiro.io.core.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginListItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		if (parent.getChildAdapterPosition(view) == 0)
		{
			outRect.top = this.space
		}
		outRect.left =  this.space
		outRect.right = this.space
		outRect.bottom = this.space
	}
}