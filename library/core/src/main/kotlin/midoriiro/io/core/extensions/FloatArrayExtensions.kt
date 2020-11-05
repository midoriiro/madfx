package midoriiro.io.core.extensions

fun FloatArray.clamp(minimum: Float, maximum: Float, inclusive: Boolean = true): FloatArray
{
	if(inclusive)
	{
		return this
			.filter { value -> value in minimum..maximum }
			.toFloatArray()
	}
	return this
		.filter { value -> minimum < value && value < maximum }
		.toFloatArray()
}

fun FloatArray.sort(): FloatArray
{
	this.sort(0, this.size)
	return this
}