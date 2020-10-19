package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BluetoothDeviceConnectionStateBroadcastReceiver : BroadcastReceiver(), midoriiro.io.core.interfaces.BroadcastReceiver
{
    private val filter = IntentFilter()
    var onConnectionStateChanged: ((BluetoothDevice, String) -> Unit)? = null

    init
    {
        this.filter.addAction(BluetoothDevice.ACTION_FOUND)
        this.filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        this.filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        this.filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
    }

    override fun onReceive(context: Context, intent: Intent) 
    {
        val action = intent.action
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!

        when 
        {
            BluetoothDevice.ACTION_FOUND == action -> {
                this.onConnectionStateChanged?.invoke(device, BluetoothDevice.ACTION_FOUND)
            }
            BluetoothDevice.ACTION_ACL_CONNECTED == action -> {
                this.onConnectionStateChanged?.invoke(device, BluetoothDevice.ACTION_ACL_CONNECTED)
            }
            BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED == action -> {
                this.onConnectionStateChanged?.invoke(device, BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED == action -> {
                this.onConnectionStateChanged?.invoke(device, BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        }
    }
    
    override fun register(context: Context)
    {
        context.registerReceiver(this, this.filter)
    }
}