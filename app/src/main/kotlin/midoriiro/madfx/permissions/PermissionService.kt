package midoriiro.madfx.permissions

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import midoriiro.madfx.location.broadcastreceivers.LocationServiceStateBroadcastReceiver
import midoriiro.madfx.location.services.LocationService
import midoriiro.madfx.bluetooth.broadcastreceviers.BluetoothStateBroadcastReceiver
import midoriiro.madfx.bluetooth.services.BluetoothService
import midoriiro.io.core.extensions.hasPermission

class PermissionService(private val context: Context)
{
	private val _bluetoothService by lazy {
		BluetoothService()
	}
	
	private val _locationService by lazy {
		LocationService()
	}
	
	private val _bluetoothBroadcastReceiver by lazy {
		BluetoothStateBroadcastReceiver()
	}
	
	private val _locationBroadcastReceiver by lazy {
		LocationServiceStateBroadcastReceiver()
	}
	
	val isBluetoothServiceEnabled: Boolean
		get() = this._bluetoothService.isEnabled
	
	val isLocationServiceEnabled: Boolean
		get() = this._locationService.isEnabled
	
	val isLocationPermissionGranted
		get() = this.context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
	
	val isRecordAudioPermissionGranted
		get() = this.context.hasPermission(Manifest.permission.RECORD_AUDIO)
	
	val isGranted: Boolean
		get() = this.isBluetoothServiceEnabled &&
				this.isLocationServiceEnabled &&
				this.isLocationPermissionGranted &&
				this.isRecordAudioPermissionGranted
	
	var pause: Boolean = false
	
	var onPermissionStateChanged: ((Boolean) -> Unit)? = null
	var onBluetoothServiceStateChanged: ((Boolean) -> Unit)? = null
	var onLocationServiceStateChanged: ((Boolean) -> Unit)? = null
	
	init
	{
		this._bluetoothBroadcastReceiver.onStateChanged = { state ->
			when(state)
			{
				BluetoothAdapter.STATE_ON -> {
					this.onPermissionStateChanged()
					this.onBluetoothServiceStateChanged?.invoke(true)
				}
				BluetoothAdapter.STATE_OFF -> {
					this.onPermissionStateChanged()
					this.onBluetoothServiceStateChanged?.invoke(false)
				}
			}
		}
		this._bluetoothBroadcastReceiver.register(this.context)
		this._locationBroadcastReceiver.onStateChanged = { state ->
			this.onPermissionStateChanged()
			this.onLocationServiceStateChanged?.invoke(state)
		}
		this._locationBroadcastReceiver.register(this.context)
	}
	
	fun close()
	{
		this.context.unregisterReceiver(this._bluetoothBroadcastReceiver)
		this.context.unregisterReceiver(this._locationBroadcastReceiver)
	}
	
	private fun onPermissionStateChanged()
	{
		if(this.pause)
		{
			return
		}
		
		this.onPermissionStateChanged?.invoke(this.isGranted)
	}
}