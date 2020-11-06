package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import midoriiro.io.core.receivers.BroadcastReceiver

class BluetoothDeviceConnectionStateBroadcastReceiver : BroadcastReceiver()
{
    var onConnectionStateChanged: ((BluetoothDevice, String) -> Unit)? = null

    init
    {
        this._filter.addAction(BluetoothDevice.ACTION_FOUND)
        this._filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        this._filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        this._filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
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
}