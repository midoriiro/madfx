package midoriiro.madfx.core.utils

class MathUtils
{
	companion object
	{
		fun map(value: Float, a: Float, b: Float, c: Float, d: Float): Float
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
		fun map(value: Double, a: Double, b: Double, c: Double, d: Double): Double
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
		fun map(value: Int, a: Int, b: Int, c: Int, d: Int): Int
		{
			return (value - a) / (b - a) * (d-c) + c
		}
	}
}