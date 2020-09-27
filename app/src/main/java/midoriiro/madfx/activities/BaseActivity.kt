package midoriiro.madfx.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import midoriiro.madfx.permissions.PermissionService
import midoriiro.madfx.bluetooth.events.BluetoothStateEvent
import org.greenrobot.eventbus.EventBus

private const val ACTIVITY_PERMISSION_REQUEST_CODE = 1

abstract class BaseActivity : AppCompatActivity()
{
	private val _service: PermissionService by lazy {
		PermissionService(this)
	}
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.onPermissionsChanged(this._service.isGranted)
		this._service.onPermissionStateChanged = { state ->
			this.onPermissionsChanged(state)
		}
	}
	
	override fun onStart()
	{
		super.onStart()
		if(this._service.isGranted)
		{
			this.onPermissionsGranted()
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		super.onActivityResult(requestCode, resultCode, data)
		if(requestCode == ACTIVITY_PERMISSION_REQUEST_CODE)
		{
			when
			{
				resultCode != Activity.RESULT_OK ->
				{
					this.onPermissionsNotGranted()
				}
				resultCode == Activity.RESULT_OK ->
				{
					this._service.pause = false
					this.onPermissionsGranted()
				}
			}
		}
	}
	
	private fun onPermissionsChanged(state: Boolean)
	{
		if (state)
		{
			this.onPermissionsGranted()
		}
		else
		{
			
			this.onPermissionsNotGranted()
		}
	}
	
	private fun onPermissionsGranted()
	{
		EventBus.getDefault().post(BluetoothStateEvent(true))
	}
	
	private fun onPermissionsNotGranted()
	{
		this._service.pause = true
		EventBus.getDefault().post(BluetoothStateEvent(false))
		val intent = Intent(this, PermissionActivity::class.java)
		this.startActivityForResult(intent, ACTIVITY_PERMISSION_REQUEST_CODE)
	}
}