package midoriiro.madfx.bluetooth.models

import android.bluetooth.BluetoothDevice
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikepenz.iconics.view.IconicsImageView
import midoriiro.madfx.bluetooth.enums.BluetoothDeviceTypes
import midoriiro.madfx.bluetooth.extensions.type

class BluetoothDeviceViewModel : ViewModel()
{
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    
    val address: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    
    val codec: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    
    val isConnected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    
    val isPlaying: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    
    val batteryLevel: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    
    val type: MutableLiveData<BluetoothDeviceTypes> by lazy {
        MutableLiveData<BluetoothDeviceTypes>()
    }
    
    fun fromDevice(device: BluetoothDevice)
    {
        this.name.value = device.name
        this.address.value = device.address
        this.codec.value = ""
        this.isConnected.value = false
        this.isPlaying.value = false
        this.batteryLevel.value = -1
        this.type.value = device.type()
    }
}

@BindingAdapter("app:iiv_icon")
fun setIcon(view: IconicsImageView, icon: String) {
    view.icon!!.icon(icon)
}

