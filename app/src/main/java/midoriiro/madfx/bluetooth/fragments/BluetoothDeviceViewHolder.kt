package midoriiro.madfx.bluetooth.fragments

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import midoriiro.madfx.R
import midoriiro.madfx.bluetooth.extensions.toViewModel
import midoriiro.madfx.databinding.ListItemBluetoothDeviceBinding
import midoriiro.madfx.bluetooth.enums.BluetoothDeviceTypes
import midoriiro.madfx.bluetooth.services.BluetoothDeviceService

class BluetoothDeviceViewHolder(val view: View) : RecyclerView.ViewHolder(view)
{
	private val _binding: ListItemBluetoothDeviceBinding = DataBindingUtil.bind(this.view)!!
	private val _service = BluetoothDeviceService() // TODO make service available for all devices, avoid one instance per device
	
	init
	{
		this._binding.lifecycleOwner = this.view.context as AppCompatActivity
	}
	
	fun bind(device: BluetoothDevice)
	{
		this._binding.model = device.toViewModel()
		
		// TODO change followings
		this._binding.model!!.codec.value = this._service.getCodec(device)
		
		this.setConnectionState(false)
		this.setImage()
		this.setBatteryLevel(this._service.getBatteryLevel())
		this.onConnectionStateChanged(this._service.getConnectionState(device))
		this._service.onConnectionStateChanged = { observedDevice, state ->
			if(device.address == observedDevice.address)
			{
				this.onConnectionStateChanged(state)
			}
		}
		this.setPlayingState(this._service.getPlayingState(device))
		this._service.onPlayingStateChanged = { observedDevice, state ->
			if(device.address == observedDevice.address)
			{
				this.onPlayingStateChanged(state)
			}
		}
		this._service.onBatteryLevelChanged = { observedDevice, level ->
			if(device.address == observedDevice.address)
			{
				Log.d("FFF", "${device.name} -> battery level $level %")
				this.setBatteryLevel(level)
			}
		}
	}
	
	private fun onConnectionStateChanged(state: Int)
	{
		if(state == BluetoothProfile.STATE_CONNECTED)
		{
			this.setConnectionState(true)
		}
		else
		{
			this.setConnectionState(false)
		}
	}
	
	private fun onPlayingStateChanged(state: Int)
	{
		if(state == BluetoothA2dp.STATE_PLAYING)
		{
			this.setPlayingState(true)
		}
		else
		{
			this.setPlayingState(false)
		}
	}
	
	private fun setConnectionState(state: Boolean)
	{
		this._binding.model!!.isConnected.value = state
	}
	
	private fun setPlayingState(state: Boolean)
	{
		this._binding.model!!.isPlaying.value = state
	}
	
	private fun setImage()
	{
		val drawable = when(this._binding.model!!.type.value)
		{
			BluetoothDeviceTypes.Headphones ->
			{
				R.drawable.device_headphones
			}
			BluetoothDeviceTypes.EarBuds ->
			{
				R.drawable.device_earbuds
			}
			BluetoothDeviceTypes.Speaker ->
			{
				R.drawable.device_speaker
			}
			else -> R.drawable.device_unknow
		}

		this._binding.image.setImageResource(drawable)
	}
	
	private fun setBatteryLevel(level: Int)
	{
		this._binding.model!!.batteryLevel.value = level

		if(level < 0)
		{
			this._binding.batteryPercentage.visibility = View.INVISIBLE
		}
		else
		{
			this._binding.batteryPercentage.visibility = View.VISIBLE
		}

		when(level)
		{
			in 0..10 ->
			{
				this._binding.batteryIcon.icon!!.icon(CommunityMaterial.Icon.cmd_battery_alert)
				this._binding.batteryIcon.icon!!.color(IconicsColor.colorInt(Color.parseColor("#ff8a65")))
			}
			in 11..20 ->
			{
				this._binding.batteryIcon.icon!!.icon(CommunityMaterial.Icon.cmd_battery_alert)
				this._binding.batteryIcon.icon!!.color(IconicsColor.colorInt(Color.parseColor("#ffd54f")))
			}
			in 21..100 ->
			{
				this._binding.batteryIcon.icon!!.icon(CommunityMaterial.Icon.cmd_battery)
				this._binding.batteryIcon.icon!!.color(IconicsColor.colorInt(Color.parseColor("#aed581")))
			}
			else ->
			{
				this._binding.batteryIcon.icon!!.icon(CommunityMaterial.Icon.cmd_battery_unknown)
				this._binding.batteryIcon.icon!!.color(IconicsColor.colorRes(R.color.material_on_surface_disabled))
			}
		}
	}
}