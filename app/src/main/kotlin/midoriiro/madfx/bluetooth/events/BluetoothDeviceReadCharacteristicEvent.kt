package midoriiro.madfx.bluetooth.events

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic

class BluetoothDeviceReadCharacteristicEvent(val device: BluetoothDevice, val characteristic: BluetoothGattCharacteristic)