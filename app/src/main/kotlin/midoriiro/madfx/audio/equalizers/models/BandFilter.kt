package midoriiro.madfx.audio.equalizers.models

import midoriiro.madfx.audio.equalizers.filters.BiQuadraticFilter

class BandFilter(
	type: BiQuadraticFilter.Type,
	frequency: Double,
	gain: Double,
	width: Double,
	rate: Double
)
{
	private var _isEnabled = false
	private var _isSelected = false
	private val _filter = BiQuadraticFilter(
		type, frequency, gain, width, rate
	)

	var isEnabled: Boolean
		set(value)
		{
			this._isEnabled = value
		}
		get() = this._isEnabled

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

	var rate: Double
		set(value)
		{
			this._filter.rate = value
		}
		get() = this._filter.rate

	fun filter(sample: Double): Double
	{
		return this._filter.filter(sample)
	}
}