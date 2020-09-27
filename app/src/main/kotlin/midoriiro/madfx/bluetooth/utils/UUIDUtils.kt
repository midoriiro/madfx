package midoriiro.madfx.bluetooth.utils

import java.util.*

private const val base = "00000000-0000-1000-8000-00805f9b34fb"
private val regex = Regex("[a-fA-F0-9]{4}")

fun UUID.fromString(service: String) : UUID
{
	if(!regex.matches(service))
	{
		throw IllegalArgumentException("must be a representation of a 16 bits hexadecimal string")
	}
	
	return UUID.fromString(base.replaceRange(4, 7, service))
}