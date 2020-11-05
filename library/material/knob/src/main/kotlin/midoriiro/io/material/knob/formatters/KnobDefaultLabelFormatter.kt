package midoriiro.io.material.knob.formatters

import midoriiro.io.material.knob.interfaces.KnobLabelFormatter

class KnobDefaultLabelFormatter : KnobLabelFormatter
{
	override fun format(value: Float): String
	{
		return value.toInt().toString()
	}

	override fun minimum(value: Float): String
	{
		return value.toInt().toString()
	}

	override fun maximum(value: Float): String
	{
		return value.toInt().toString()
	}
}