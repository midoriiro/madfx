package midoriiro.madfx.location.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import midoriiro.madfx.location.services.LocationService


class LocationServiceStateBroadcastReceiver : BroadcastReceiver(), midoriiro.madfx.core.interfaces.BroadcastReceiver
{
    private val filter = IntentFilter()
    var onStateChanged: ((Boolean) -> Unit)? = null
    
    private val _service: LocationService by lazy {
        LocationService()
    }

    init
    {
        this.filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val action = intent.action
        if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val state = this._service.isEnabled
            this.onStateChanged?.invoke(state)
        }
    }
    
    override fun register(context: Context)
    {
        context.registerReceiver(this, this.filter)
    }
}