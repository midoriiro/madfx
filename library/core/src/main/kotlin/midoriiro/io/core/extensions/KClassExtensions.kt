package midoriiro.io.core.extensions

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

/**
 * Create an instance of O from a class name string
 *
 * @param O type of the class
 * @param className name of the class
 * @return a new instance of O class
 * @exception ClassNotFoundException will be throwned if className is not an existing class
 * @exception ClassNotFoundException will be throwned if className is not a subclass of the current type
 * @exception ClassNotFoundException will be throwned if className is abstract or is an interface
 */
inline fun <reified O, reified I: Any> KClass<I>.createFromString(className: String): O
{
	val klass: KClass<out Any>
	try
	{
		klass = Class.forName(className).kotlin
	}
	catch (exception: ClassNotFoundException)
	{
		throw ClassNotFoundException("class $className is not an existing class" )
	}
	when
	{
		!klass.isSubclassOf(this) ->
		{
			throw ClassNotFoundException("class $className is not subclassing ${this.qualifiedName}" )
		}
		klass.isAbstract ->
		{
			throw ClassNotFoundException("class $className is abstract or is an interface" )
		}
		else -> return klass.createInstance() as O
	}
}