package midoriiro.madfx.bluetooth.broadcastreceviers

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAssignedNumbers
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.Context
import android.content.Intent
import android.util.Log
import midoriiro.io.core.receivers.BroadcastReceiver


class BluetoothHeadsetBroadcastReceiver : BroadcastReceiver()
{
    init
    {
        this._filter.addAction(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT)
        this._filter.addCategory(BluetoothHeadset.VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY + "." + BluetoothAssignedNumbers.APPLE)
    }
    
    fun invoke(context: Context)
    {
        val intent = Intent(context, BluetoothHeadsetBroadcastReceiver::class.java)
        context.sendBroadcast(intent)
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val action = intent.action
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!
        if (action == BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT)
        {
            val command = intent.getStringExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD)
            val commandType = intent.getIntExtra(
                BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE,
                BluetoothAdapter.ERROR
            )
            val arguments = intent.extras!!.get(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS)
            
            Log.d("FFF", "HSP -> $command $commandType $arguments")
        }
    }
}