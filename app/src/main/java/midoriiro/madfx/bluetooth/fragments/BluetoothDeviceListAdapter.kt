package midoriiro.madfx.bluetooth.fragments

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import midoriiro.madfx.R

class BluetoothDeviceListAdapter(
) : RecyclerView.Adapter<BluetoothDeviceViewHolder>()
{
	private var dataset: List<BluetoothDevice> = listOf()
	
	fun setDataset(devices: List<BluetoothDevice>)
	{
		this.dataset = devices
		this.notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder
	{
		val view = LayoutInflater
			.from(parent.context)
			.inflate(R.layout.list_item_bluetooth_device, parent, false)
		return BluetoothDeviceViewHolder(view)
	}
	
	override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int)
	{
		val item = this.dataset[position]
		holder.bind(item)
	}
	
	override fun getItemCount(): Int = this.dataset.size
}