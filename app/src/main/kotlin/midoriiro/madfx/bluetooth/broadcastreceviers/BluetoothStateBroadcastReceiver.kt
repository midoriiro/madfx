package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter


class BluetoothStateBroadcastReceiver : BroadcastReceiver(), midoriiro.madfx.core.interfaces.BroadcastReceiver
{
    private val filter = IntentFilter()
    var onStateChanged: ((Int) -> Unit)? = null

    init
    {
        this.filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val action = intent.action
        if (action == BluetoothAdapter.ACTION_STATE_CHANGED)
        {
            val state = intent.getIntExtra(
                BluetoothAdapter.EXTRA_STATE,
                BluetoothAdapter.ERROR
            )
            this.onStateChanged?.invoke(state)
        }
    }
    
    override fun register(context: Context)
    {
        context.registerReceiver(this, this.filter)
    }
}