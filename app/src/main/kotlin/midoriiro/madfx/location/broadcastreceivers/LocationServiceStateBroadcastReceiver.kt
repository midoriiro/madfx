package midoriiro.madfx.location.broadcastreceivers

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import midoriiro.io.core.receivers.BroadcastReceiver
import midoriiro.madfx.location.services.LocationService


class LocationServiceStateBroadcastReceiver : BroadcastReceiver()
{
    var onStateChanged: ((Boolean) -> Unit)? = null
    
    private val _service: LocationService by lazy {
        LocationService()
    }

    init
    {
        this._filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val action = intent.action
        if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val state = this._service.isEnabled
            this.onStateChanged?.invoke(state)
        }
    }
}