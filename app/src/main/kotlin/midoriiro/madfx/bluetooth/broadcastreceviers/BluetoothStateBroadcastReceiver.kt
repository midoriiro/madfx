package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import midoriiro.io.core.receivers.BroadcastReceiver


class BluetoothStateBroadcastReceiver : BroadcastReceiver()
{
    var onStateChanged: ((Int) -> Unit)? = null

    init
    {
        this._filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
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
}