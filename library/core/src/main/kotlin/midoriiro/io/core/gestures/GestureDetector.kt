package midoriiro.io.core.gestures

import android.content.Context
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent

class GestureDetector : GestureDetector
{
	private var listener: midoriiro.io.core.gestures.SimpleOnGestureListener? = null

	constructor(
		context: Context?,
		listener: OnGestureListener?
	) : super(context, listener)
	{
		this.init(listener)
	}

	constructor(
		context: Context?,
		listener: OnGestureListener?,
		handler: Handler?
	) : super(context, listener, handler)
	{
		this.init(listener)
	}

	constructor(
		context: Context?,
		listener: OnGestureListener?,
		handler: Handler?,
		unused: Boolean
	) : super(context, listener, handler, unused)
	{
		this.init(listener)
	}

	private fun init(listener: OnGestureListener?)
	{
		if(listener is midoriiro.io.core.gestures.SimpleOnGestureListener)
		{
			this.listener = listener
		}
	}

	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		return when
		{
			super.onTouchEvent(event) -> true
			this.listener != null && event.action == MotionEvent.ACTION_UP -> this.listener!!.onUp(event)
			this.listener != null && event.action == MotionEvent.ACTION_MOVE -> this.listener!!.onMove(event)
			else -> false
		}
	}
}
