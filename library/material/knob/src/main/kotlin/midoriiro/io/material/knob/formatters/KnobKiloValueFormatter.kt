package midoriiro.io.material.knob.formatters

import midoriiro.io.material.knob.interfaces.KnobValueFormatter

class KnobKiloValueFormatter : KnobValueFormatter
{
	override fun format(value: Float): String
	{
		if(value >= 1000)
		{
			return String.format("%.3f k", value / 1000f)
		}
		return String.format("%.3f", value)
	}
}