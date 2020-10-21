package midoriiro.io.core.gestures

import android.view.GestureDetector
import android.view.MotionEvent

open class SimpleOnGestureListener : GestureDetector.SimpleOnGestureListener()
{
	open fun onUp(event: MotionEvent): Boolean
	{
		return false
	}

	open fun onMove(event: MotionEvent): Boolean
	{
		return false
	}

	override fun onDown(event: MotionEvent): Boolean
	{
		return super.onDown(event)
	}

	override fun onShowPress(event: MotionEvent)
	{
		super.onShowPress(event)
	}

	override fun onSingleTapUp(event: MotionEvent): Boolean
	{
		return super.onSingleTapUp(event)
	}

	override fun onScroll(
		event1: MotionEvent,
		event2: MotionEvent,
		distanceX: Float,
		distanceY: Float
	): Boolean
	{
		return super.onScroll(event1, event2, distanceX, distanceY)
	}

	override fun onLongPress(event: MotionEvent)
	{
		super.onLongPress(event)
	}

	override fun onFling(
		event1: MotionEvent,
		event2: MotionEvent,
		velocityX: Float,
		velocityY: Float
	): Boolean
	{
		return super.onFling(event1, event2, velocityX, velocityY)
	}

	override fun onSingleTapConfirmed(event: MotionEvent): Boolean
	{
		return super.onSingleTapConfirmed(event)
	}

	override fun onDoubleTap(event: MotionEvent): Boolean
	{
		return super.onDoubleTap(event)
	}

	override fun onDoubleTapEvent(event: MotionEvent): Boolean
	{
		return super.onDoubleTapEvent(event)
	}

	override fun onContextClick(event: MotionEvent): Boolean
	{
		return super.onContextClick(event)
	}
}
