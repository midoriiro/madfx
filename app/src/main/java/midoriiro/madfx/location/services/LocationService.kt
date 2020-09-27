package midoriiro.madfx.location.services

import android.content.Context
import android.location.LocationManager
import midoriiro.madfx.core.Application


class LocationService
{
	private val _manager by lazy {
		Application.instance.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
	}
	
	val isGpsEnabled: Boolean
		get() = this._manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
	
	val isNetworkEnabled: Boolean
		get() = this._manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
	
	val isPassiveEnabled: Boolean
		get() = this._manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
	
	val isEnabled: Boolean
		get() = this.isGpsEnabled || this.isNetworkEnabled || this.isPassiveEnabled
}