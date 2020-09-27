package midoriiro.madfx.bluetooth.services

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import midoriiro.madfx.bluetooth.extensions.isAnAudioDevice
import java.util.stream.Collectors

class BluetoothService
{
    private val _adapter = BluetoothAdapter.getDefaultAdapter()

    val isEnabled: Boolean
        get() = this._adapter.isEnabled

    fun getPairedDevices(): List<BluetoothDevice>?
    {
        if(!this.isEnabled)
        {
            return null
        }

        return this._adapter.bondedDevices
            .stream()
            .filter { x -> x.isAnAudioDevice() }
            .collect(Collectors.toList())
    }
    
    fun getRemoteDevice(address: String): BluetoothDevice
    {
        return this._adapter.getRemoteDevice(address)
    }
}