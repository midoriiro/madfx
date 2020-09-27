package midoriiro.madfx.bluetooth.services

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import midoriiro.madfx.bluetooth.broadcastreceviers.BluetoothDeviceBatteryLevelBroadcastReceiver
import midoriiro.madfx.bluetooth.broadcastreceviers.BluetoothDeviceConnectionStateBroadcastReceiver
import midoriiro.madfx.bluetooth.broadcastreceviers.BluetoothDevicePlayingStateBroadcastReceiver
import midoriiro.madfx.bluetooth.broadcastreceviers.BluetoothHeadsetBroadcastReceiver
import midoriiro.madfx.core.Application

class BluetoothDeviceService
{
	private val _profile = Application.instance.a2dpProfile
	private val _connectionStateBroadcastReceiver = BluetoothDeviceConnectionStateBroadcastReceiver()
	private val _playingStateBroadcastReceiver = BluetoothDevicePlayingStateBroadcastReceiver()
	private val _batteryLevelBroadcastReceiver = BluetoothDeviceBatteryLevelBroadcastReceiver()
	private val _headsetBroadcastReceiver = BluetoothHeadsetBroadcastReceiver()
	
	var onConnectionStateChanged: ((BluetoothDevice, Int) -> Unit)? = null
	var onPlayingStateChanged: ((BluetoothDevice, Int) -> Unit)? = null
	var onBatteryLevelChanged: ((BluetoothDevice, Int) -> Unit)? = null
	
	init
	{
		this._connectionStateBroadcastReceiver.onConnectionStateChanged = { device, state ->
			when(state)
			{
				BluetoothDevice.ACTION_ACL_CONNECTED -> {
					this.onConnectionStateChanged?.invoke(device, BluetoothProfile.STATE_CONNECTED)
				}
				BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
					this.onConnectionStateChanged?.invoke(device, BluetoothProfile.STATE_DISCONNECTED)
				}
			}
		}
		this._connectionStateBroadcastReceiver.register(Application.instance.applicationContext)
		
		this._playingStateBroadcastReceiver.onPlayingStateChanged = { device, state ->
			this.onPlayingStateChanged?.invoke(device, state)
		}
		this._playingStateBroadcastReceiver.register(Application.instance.applicationContext)
		
		this._batteryLevelBroadcastReceiver.onLevelChanged = { device, level ->
			this.onBatteryLevelChanged?.invoke(device, level)
		}
		this._batteryLevelBroadcastReceiver.register(Application.instance.applicationContext)
		
		this._headsetBroadcastReceiver.register(Application.instance.applicationContext)
	}
	
	protected fun finalize()
	{
		Application.instance.unregisterReceiver(this._connectionStateBroadcastReceiver)
		Application.instance.unregisterReceiver(this._playingStateBroadcastReceiver)
		Application.instance.unregisterReceiver(this._batteryLevelBroadcastReceiver)
		Application.instance.unregisterReceiver(this._headsetBroadcastReceiver)
	}
	
	fun getConnectionState(device: BluetoothDevice): Int
	{
		if(this._profile == null)
		{
			return BluetoothProfile.STATE_DISCONNECTED
		}
		
		return this._profile.getConnectionState(device)
	}
	
	fun getPlayingState(device: BluetoothDevice): Boolean
	{
		if(this._profile == null)
		{
			return false
		}
		
		return this._profile.isA2dpPlaying(device)
	}
	
	fun getBatteryLevel(): Int
	{
		return -1
	}
	
	fun getCodec(device: BluetoothDevice): String
	{
		return "SBC"
	}
}