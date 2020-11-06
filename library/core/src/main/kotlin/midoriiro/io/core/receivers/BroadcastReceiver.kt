package midoriiro.io.core.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

abstract class BroadcastReceiver : BroadcastReceiver()
{
	protected val _filter = IntentFilter()

	fun register(context: Context)
	{
		context.registerReceiver(this, this._filter)
	}

	fun unregister(context: Context)
	{
		context.unregisterReceiver(this)
	}
}