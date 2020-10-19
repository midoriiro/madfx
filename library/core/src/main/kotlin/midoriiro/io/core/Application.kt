package midoriiro.io.core

import android.app.Application
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile

open class Application : Application()
{
	companion object Singleton
	{
		lateinit var instance: midoriiro.io.core.Application
	}
	
	override fun onCreate() {
		super.onCreate()
		instance = this
	}
}
