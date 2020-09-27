package midoriiro.madfx.core.extensions

import android.app.Activity
import androidx.core.app.ActivityCompat

fun Activity.requestPermission(permission: String, requestCode: Int)
{
	ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
}

fun Activity.requestPermission(permissions: Set<String>, requestCode: Int)
{
	ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
}