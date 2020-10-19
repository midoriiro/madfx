package midoriiro.madfx.bluetooth.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import midoriiro.madfx.R
import midoriiro.io.core.decorations.MarginGridItemDecoration
import midoriiro.io.core.decorations.MarginListItemDecoration
import midoriiro.madfx.databinding.FragmentBluetoothDeviceListBinding
import midoriiro.madfx.bluetooth.events.BluetoothStateEvent
import midoriiro.madfx.bluetooth.services.BluetoothService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BluetoothDeviceListFragment : Fragment()
{
    private lateinit var _binding: FragmentBluetoothDeviceListBinding
    
    private val _service by lazy {
        BluetoothService()
    }
    private val _adapter = BluetoothDeviceListAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }
    
    override fun onDestroy()
    {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_bluetooth_device_list, container, false)
        this._binding = DataBindingUtil.bind(view)!!
        this._binding.devices.adapter = this._adapter
    
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            this._binding.devices.addItemDecoration(MarginListItemDecoration(16))
            this._binding.devices.layoutManager = LinearLayoutManager(this.activity)
        }
        else
        {
            this._binding.devices.addItemDecoration(MarginGridItemDecoration(16))
            this._binding.devices.layoutManager = GridLayoutManager(this.activity, 2)
        }

        return view;
    }
    
    @Subscribe()
    fun onBluetoothStateChanged(event: BluetoothStateEvent)
    {
        when(event.enabled)
        {
            true -> this.showList()
            false -> this.hideList()
        }
    }

    private fun showList()
    {
        this._adapter.setDataset(this._service.getPairedDevices()!!)
        this._binding.devices.visibility = View.VISIBLE
        this._binding.message.visibility = View.GONE
    }

    private fun hideList()
    {
        this._binding.devices.visibility = View.GONE
        this._binding.message.visibility = View.VISIBLE
    }
}