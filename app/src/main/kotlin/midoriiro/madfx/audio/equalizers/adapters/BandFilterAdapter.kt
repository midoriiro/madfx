package midoriiro.madfx.audio.equalizers.adapters

import midoriiro.madfx.audio.equalizers.filters.BiQuadraticFilter
import midoriiro.madfx.audio.equalizers.models.BandFilter

class BandFilterAdapter
{
	open class Listener
	{
		open fun onItemInserted(index: Int){}
		open fun onItemRemoved(index: Int){}
		open fun onItemTypeChanged(index: Int, type: BiQuadraticFilter.Type){}
		open fun onItemFrequencyChanged(index: Int, frequency: Double){}
		open fun onItemGainChanged(index: Int, gain: Double){}
		open fun onItemWidthChanged(index: Int, width: Double){}
		open fun onItemRateChanged(index: Int, rate: Double){}
		open fun onItemEnabledChanged(index: Int, state: Boolean){}
		open fun onItemSelectionChanged(){}
	}

	private val _bands = mutableListOf<BandFilter>()
	private val _listeners = mutableListOf<Listener>()

	private fun isValidIndexOrThrow(index: Int)
	{
		if(!(0 until this._bands.size).contains(index))
		{
			throw IndexOutOfBoundsException()
		}
	}

	fun addListener(listener: Listener)
	{
		this._listeners.add(listener)
	}

	fun removeListener(listener: Listener)
	{
		if(!this._listeners.contains(listener))
		{
			return
		}
		this._listeners.remove(listener)
	}

	fun add(band: BandFilter)
	{
		this._bands.add(band)
		val index = this._bands.indexOf(band)
		this._listeners.forEach { it.onItemInserted(index) }
	}

	fun remove(band: BandFilter)
	{
		if(!this._bands.contains(band))
		{
			return
		}
		this._bands.remove(band)
		val index = this._bands.indexOf(band)
		this._listeners.forEach { it.onItemRemoved(index) }
	}

	fun get(index: Int): BandFilter
	{
		this.isValidIndexOrThrow(index)
		return this._bands[index]
	}

	fun size(): Int
	{
		return this._bands.size
	}

	fun setEnabled(index: Int, state: Boolean)
	{
		this.isValidIndexOrThrow(index)
		this._bands[index].isEnabled = state
		this._listeners.forEach { it.onItemEnabledChanged(index, state) }
	}

	fun setSelected(index: Int)
	{
		this.isValidIndexOrThrow(index)
		this._bands.forEach { band -> band.isSelected = false }
		this._bands[index].isSelected = true
		this._listeners.forEach { it.onItemSelectionChanged() }
	}

	fun setType(index: Int, type: BiQuadraticFilter.Type)
	{
		this.isValidIndexOrThrow(index)
		this._bands[index].type = type
		this._listeners.forEach { it.onItemTypeChanged(index, type) }
	}

	fun setFrequency(index: Int, frequency: Double)
	{
		this.isValidIndexOrThrow(index)
		this._bands[index].frequency = frequency
		this._listeners.forEach { it.onItemFrequencyChanged(index, frequency) }
	}

	fun setGain(index: Int, gain: Double)
	{
		this.isValidIndexOrThrow(index)
		this._bands[index].gain = gain
		this._listeners.forEach { it.onItemGainChanged(index, gain) }
	}

	fun setWidth(index: Int, width: Double)
	{
		this.isValidIndexOrThrow(index)
		this._bands[index].width = width
		this._listeners.forEach { it.onItemWidthChanged(index, width) }
	}

	fun setRate(index: Int, rate: Double)
	{
		this.isValidIndexOrThrow(index)
		this._bands[index].rate = rate
		this._listeners.forEach { it.onItemRateChanged(index, rate) }
	}
}