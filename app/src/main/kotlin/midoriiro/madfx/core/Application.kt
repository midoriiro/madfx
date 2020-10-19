package midoriiro.madfx.core

import android.app.Application
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile

class Application : midoriiro.io.core.Application()
{
	private var _headsetProfile: BluetoothHeadset? = null
	private var _a2dpProfile: BluetoothA2dp? = null
	
	val headsetProfile: BluetoothHeadset?
		get() = this._headsetProfile
	
	val a2dpProfile: BluetoothA2dp?
		get() = this._a2dpProfile

	companion object Singleton
	{
		lateinit var instance: midoriiro.madfx.core.Application
	}
	
	override fun onCreate() {
		super.onCreate()
		instance = this

		BluetoothAdapter.getDefaultAdapter().getProfileProxy(
			this.applicationContext,
			object: BluetoothProfile.ServiceListener{
				override fun onServiceConnected(profile: Int, proxy: BluetoothProfile)
				{
					this@Application._headsetProfile = proxy as BluetoothHeadset
				}
				
				override fun onServiceDisconnected(profile: Int)
				{
					this@Application._headsetProfile = null
				}
				
			},
			BluetoothProfile.HEADSET
		)
		
		BluetoothAdapter.getDefaultAdapter().getProfileProxy(
			this.applicationContext,
			object: BluetoothProfile.ServiceListener{
				override fun onServiceConnected(profile: Int, proxy: BluetoothProfile)
				{
					this@Application._a2dpProfile = proxy as BluetoothA2dp
				}
				
				override fun onServiceDisconnected(profile: Int)
				{
					this@Application._a2dpProfile = null
				}
				
			},
			BluetoothProfile.A2DP
		)
	}
}
