package midoriiro.io.material.knob.formatters

import midoriiro.io.material.knob.interfaces.KnobValueFormatter

class KnobIntValueFormatter : KnobValueFormatter
{
	override fun format(value: Float): String
	{
		return value.toInt().toString()
	}
}