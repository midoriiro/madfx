package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import midoriiro.io.core.receivers.BroadcastReceiver


class BluetoothDeviceBatteryLevelBroadcastReceiver : BroadcastReceiver()
{
    private val ACTION_BATTERY_LEVEL_CHANGED = "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED"
    private val EXTRA_BATTERY_LEVEL = "android.bluetooth.device.extra.BATTERY_LEVEL"

    var onLevelChanged: ((BluetoothDevice, Int) -> Unit)? = null

    init
    {
        this._filter.addAction(this.ACTION_BATTERY_LEVEL_CHANGED)
    }
    
    fun invoke(context: Context)
    {
        val intent = Intent(context, BluetoothDeviceBatteryLevelBroadcastReceiver::class.java)
        context.sendBroadcast(intent)
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val action = intent.action
        if (action == this.ACTION_BATTERY_LEVEL_CHANGED)
        {
	        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!
            val level = intent.getIntExtra(
                this.EXTRA_BATTERY_LEVEL,
                BluetoothAdapter.ERROR
            )
            this.onLevelChanged?.invoke(device, level)
        }
    }
}