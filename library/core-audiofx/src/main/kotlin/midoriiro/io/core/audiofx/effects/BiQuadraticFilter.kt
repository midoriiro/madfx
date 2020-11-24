package midoriiro.io.core.audiofx.effects

interface BiQuadraticFilter
{
	enum class Type(val value: Int)
	{
		LOW_PASS(0),
		HIGH_PASS(1),
		BAND_PASS(2),
		BELL(3),
		NOTCH(4),
		LOW_SHELF(5),
		HIGH_SHELF(6);
	}
	var type: BiQuadraticFilter.Type
	var frequency: Double
	var gain: Double
	var width: Double
	var enabled: Boolean
	fun amplitude(frequency: Double): Double
}