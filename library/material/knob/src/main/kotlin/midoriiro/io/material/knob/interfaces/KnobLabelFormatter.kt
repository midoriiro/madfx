package midoriiro.io.material.knob.interfaces

interface KnobLabelFormatter
{
	fun format(value: Float): String
	fun minimum(value: Float): String
	fun maximum(value: Float): String
}