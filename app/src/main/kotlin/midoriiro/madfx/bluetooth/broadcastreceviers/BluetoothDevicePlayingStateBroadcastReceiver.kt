package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BluetoothDevicePlayingStateBroadcastReceiver : BroadcastReceiver(), midoriiro.io.core.interfaces.BroadcastReceiver
{
    private val filter = IntentFilter()
    var onPlayingStateChanged: ((BluetoothDevice, Int) -> Unit)? = null

    init
    {
        this.filter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED)
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val action = intent.action
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!
        if (BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED == action)
        {
            val state = intent.getIntExtra(
                BluetoothProfile.EXTRA_STATE,
                BluetoothAdapter.ERROR
            )
            this.onPlayingStateChanged?.invoke(device, state)
        }
    }
    
    override fun register(context: Context)
    {
        context.registerReceiver(this, this.filter)
    }
}