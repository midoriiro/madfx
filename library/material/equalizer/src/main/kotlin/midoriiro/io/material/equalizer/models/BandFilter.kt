package midoriiro.io.material.equalizer.models

import midoriiro.io.core.audiofx.effects.BiQuadraticFilter

class BandFilter(
	filter: BiQuadraticFilter
)
{
	private var _isSelected = false
	private val _filter = filter

	var isEnabled: Boolean
		set(value)
		{
			this._filter.enabled = value
		}
		get() = this._filter.enabled

	var isSelected: Boolean
		set(value)
		{
			this._isSelected = value
		}
		get() = this._isSelected

	var type: BiQuadraticFilter.Type
		set(value)
		{
			this._filter.type = value
		}
		get() = this._filter.type

	var frequency: Double
		set(value)
		{
			this._filter.frequency = value
		}
		get() = this._filter.frequency

	var gain: Double
		set(value)
		{
			this._filter.gain = value
		}
		get() = this._filter.gain

	var width: Double
		set(value)
		{
			this._filter.width = value
		}
		get() = this._filter.width

	fun amplitude(frequency: Double): Double
	{
		return this._filter.amplitude(frequency)
	}
}