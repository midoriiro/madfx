package midoriiro.io.core.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasPermission(permissionType: String): Boolean {
	return ContextCompat.checkSelfPermission(this, permissionType) == PackageManager.PERMISSION_GRANTED
}