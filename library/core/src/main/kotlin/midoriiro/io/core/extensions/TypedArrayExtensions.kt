package midoriiro.io.core.extensions

import android.content.res.TypedArray
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

inline fun <reified T: Enum<T>> TypedArray.getEnum(index: Int, defValue: T): T
{
	return enumValues<T>()[this.getInt(
		index,
		defValue.ordinal
	)]
}

inline fun <reified T: Any> TypedArray.getClass(index: Int, defValue: KClass<T>): T
{
	val className = this.getString(index)
	return if(className.isNullOrEmpty())
	{
		defValue.createInstance()
	}
	else
	{
		T::class.createFromString(className)
	}
}

fun TypedArray.getLong(index: Int, defValue: Int): Long
{
	return this.getInt(index, defValue).toLong()
}