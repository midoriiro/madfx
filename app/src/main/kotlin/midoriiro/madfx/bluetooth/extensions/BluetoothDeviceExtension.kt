package midoriiro.madfx.bluetooth.extensions

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import midoriiro.madfx.bluetooth.enums.BluetoothDeviceTypes
import midoriiro.madfx.bluetooth.models.BluetoothDeviceViewModel

fun BluetoothDevice.toViewModel(): BluetoothDeviceViewModel
{
    val model = BluetoothDeviceViewModel()
    model.fromDevice(this)
    return model
}

fun BluetoothDevice.type(): BluetoothDeviceTypes
{
    when (this.bluetoothClass.deviceClass)
    {
        BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES ->
        {
            return BluetoothDeviceTypes.Headphones
        }
        BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER ->
        {
            return BluetoothDeviceTypes.Speaker
        }
        BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET ->
        {
            return BluetoothDeviceTypes.EarBuds
        }
        else -> return BluetoothDeviceTypes.Unknown
    }
}

fun BluetoothDevice.isAnAudioDevice(): Boolean
{
    return this.bluetoothClass.majorDeviceClass == BluetoothClass.Device.Major.AUDIO_VIDEO
}