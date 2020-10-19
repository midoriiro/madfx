package midoriiro.madfx.activities

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import kotlinx.android.synthetic.main.permissions_actions.view.*
import midoriiro.madfx.R
import midoriiro.io.core.extensions.requestPermission
import midoriiro.madfx.databinding.ActivityPermissionsBinding
import midoriiro.madfx.permissions.PermissionService

private const val BLUETOOTH_ENABLE_REQUEST_CODE = 1
private const val LOCATION_ENABLE_REQUEST_CODE = 2
private const val LOCATION_PERMISSION_REQUEST_CODE = 3
private const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 4

class PermissionActivity : AppCompatActivity(R.layout.activity_permissions)
{
	private val _service: PermissionService by lazy {
		PermissionService(this)
	}
	
	private lateinit var _binding: ActivityPermissionsBinding
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		
		this.supportActionBar?.hide()
		
		this._binding = DataBindingUtil.setContentView(this, R.layout.activity_permissions)
		this._binding.actions.card_bluetooth_permission.setOnClickListener { this.onClickBluetoothPermission() }
		this._binding.actions.card_location_service.setOnClickListener { this.onClickLocationService() }
		this._binding.actions.card_location_permission.setOnClickListener { this.onClickLocationPermission() }
		this._binding.actions.card_record_audio_permission.setOnClickListener { this.onClickRecordAudioPermission() }
		
		this._service.onBluetoothServiceStateChanged = { state ->
			when(state)
			{
				true -> this.onBluetoothServiceEnabled()
				false -> this.onBluetoothServiceDisabled()
			}
		}
		this._service.onLocationServiceStateChanged = { state ->
			when(state)
			{
				true -> this.onLocationServiceEnabled()
				false -> this.onLocationServiceDisabled()
			}
		}
	}
	
	override fun onDestroy()
	{
		super.onDestroy()
		this._service.close()
	}
	
	override fun onResume()
	{
		super.onResume()
		
		this.onGranted()
		
		if (this._service.isBluetoothServiceEnabled)
		{
			this.onBluetoothServiceEnabled()
		}
		else
		{
			this.onBluetoothServiceDisabled()
		}
		
		if (this._service.isLocationServiceEnabled)
		{
			this.onLocationServiceEnabled()
		}
		else
		{
			this.onLocationServiceDisabled()
		}
		
		if (this._service.isLocationPermissionGranted)
		{
			this.onLocationPermissionEnabled()
		}
		else
		{
			this.onLocationPermissionDisabled()
		}
		
		if (this._service.isRecordAudioPermissionGranted)
		{
			this.onRecordAudioPermissionEnabled()
		}
		else
		{
			this.onRecordAudioPermissionDisabled()
		}
	}
	
	override fun onBackPressed() {}
	
	private fun onClickBluetoothPermission()
	{
		this.requestEnableBluetoothService()
	}
	
	private fun onClickLocationService()
	{
		this.requestEnableLocationService()
	}
	
	private fun onClickLocationPermission()
	{
		this.requestLocationPermission()
	}
	
	private fun onClickRecordAudioPermission()
	{
		this.requestRecordAudioPermission()
	}
	
	private fun onBluetoothServiceEnabled()
	{
		this._binding.actions.card_bluetooth_permission.isEnabled = false
		this._binding.actions.state_bluetooth_service.visibility = View.VISIBLE
		this.onGranted()
	}
	
	private fun onLocationServiceEnabled()
	{
		this._binding.actions.card_location_service.isEnabled = false
		this._binding.actions.state_location_service.visibility = View.VISIBLE
		this.onGranted()
	}
	
	private fun onLocationPermissionEnabled()
	{
		this._binding.actions.card_location_permission.isEnabled = false
		this._binding.actions.state_location_permission.visibility = View.VISIBLE
		this.onGranted()
	}
	
	private fun onRecordAudioPermissionEnabled()
	{
		this._binding.actions.card_record_audio_permission.isEnabled = false
		this._binding.actions.state_record_audio_permission.visibility = View.VISIBLE
		this.onGranted()
	}
	
	private fun onBluetoothServiceDisabled()
	{
		this._binding.actions.card_bluetooth_permission.isEnabled = true
		this._binding.actions.state_bluetooth_service.visibility = View.GONE
	}
	
	private fun onLocationServiceDisabled()
	{
		this._binding.actions.card_location_service.isEnabled = true
		this._binding.actions.state_location_service.visibility = View.GONE
	}
	
	private fun onLocationPermissionDisabled()
	{
		this._binding.actions.card_location_permission.isEnabled = true
		this._binding.actions.state_location_permission.visibility = View.GONE
	}
	
	private fun onRecordAudioPermissionDisabled()
	{
		this._binding.actions.card_record_audio_permission.isEnabled = true
		this._binding.actions.state_record_audio_permission.visibility = View.GONE
	}
	
	private fun onGranted()
	{
		if(!this._service.isGranted)
		{
			return
		}
		this.setResult(Activity.RESULT_OK)
		finish()
	}
	
	private fun requestEnableBluetoothService()
	{
		if (this._service.isBluetoothServiceEnabled)
		{
			return
		}
		val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
		this.startActivityForResult(intent, BLUETOOTH_ENABLE_REQUEST_CODE)
	}
	
	private fun requestEnableLocationService()
	{
		if (this._service.isLocationServiceEnabled)
		{
			return
		}
		val locationRequest = LocationRequest.create()
		locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
		locationRequest.interval = 30 * 1000.toLong()
		locationRequest.fastestInterval = 5 * 1000.toLong()
		val builder = LocationSettingsRequest
			.Builder()
			.addLocationRequest(locationRequest)
			.setAlwaysShow(true)
		val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
		result.addOnCompleteListener { task ->
			try
			{
				task.getResult(ApiException::class.java)
			}
			catch (exception: ApiException)
			{
				when (exception.statusCode)
				{
					LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
					{
						val resolvable = exception as ResolvableApiException
						resolvable.startResolutionForResult(
							this, LOCATION_ENABLE_REQUEST_CODE
						)
					}
				}
			}
		}
	}
	
	private fun requestLocationPermission()
	{
		if (this._service.isLocationPermissionGranted)
		{
			return
		}
		this.requestPermission(
			setOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
			LOCATION_PERMISSION_REQUEST_CODE
		)
	}
	
	private fun requestRecordAudioPermission()
	{
		if (this._service.isRecordAudioPermissionGranted)
		{
			return
		}
		this.requestPermission(
			setOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_REQUEST_CODE
		)
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		super.onActivityResult(requestCode, resultCode, data)
		when (requestCode)
		{
			BLUETOOTH_ENABLE_REQUEST_CODE ->
			{
				when (resultCode)
				{
					Activity.RESULT_OK ->
					{
						this.onBluetoothServiceEnabled()
					}
				}
			}
			LOCATION_ENABLE_REQUEST_CODE ->
			{
				when (resultCode)
				{
					Activity.RESULT_OK ->
					{
						this.onLocationServiceEnabled()
					}
				}
			}
		}
	}
	
	override fun onRequestPermissionsResult(
		requestCode: Int, permissions: Array<out String>, grantResults: IntArray
	)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		when (requestCode)
		{
			LOCATION_PERMISSION_REQUEST_CODE ->
			{
				if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED)
				{
					this.requestLocationPermission()
				}
				else
				{
					this.onLocationPermissionEnabled()
				}
			}
			RECORD_AUDIO_PERMISSION_REQUEST_CODE ->
			{
				if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED)
				{
					this.requestRecordAudioPermission()
				}
				else
				{
					this.onRecordAudioPermissionEnabled()
				}
			}
		}
	}
}