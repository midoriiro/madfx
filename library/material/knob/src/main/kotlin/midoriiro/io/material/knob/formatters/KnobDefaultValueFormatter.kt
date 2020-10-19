package midoriiro.io.material.knob.formatters

import midoriiro.io.material.knob.interfaces.KnobValueFormatter

class KnobDefaultValueFormatter : KnobValueFormatter
{
	override fun format(value: Float): String
	{
		return String.format("%.3f", value)
	}
}