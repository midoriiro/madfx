package midoriiro.madfx.core.utils

class MathUtils
{
	companion object
	{
		fun map(value: Float, a: Float, b: Float, c: Float, d: Float): Float
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
	}
}