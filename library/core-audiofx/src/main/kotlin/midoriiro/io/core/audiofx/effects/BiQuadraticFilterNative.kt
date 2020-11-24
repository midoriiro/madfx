package midoriiro.io.core.audiofx.effects

import android.content.Context
import android.media.AudioManager
import midoriiro.io.core.Application

internal class BiQuadraticFilterNative(pointer: Long?) : BiQuadraticFilter
{
	val nativePointer: Long

	override var type: BiQuadraticFilter.Type
		set(value)
		{
			this.setType(this.nativePointer, value)
		}
		get() = this.getType(this.nativePointer)

	override var frequency: Double
		set(value)
		{
			this.setFrequency(this.nativePointer, value)
		}
		get() = this.getFrequency(this.nativePointer)

	override var gain: Double
		set(value)
		{
			this.setGain(this.nativePointer, value)
		}
		get() = this.getGain(this.nativePointer)

	override var width: Double
		set(value)
		{
			this.setWidth(this.nativePointer, value)
		}
		get() = this.getWidth(this.nativePointer)

	override var enabled: Boolean
		set(value)
		{
			this.setEnabled(this.nativePointer, value)
		}
		get() = this.getEnabled(this.nativePointer)

	companion object {
		init {
			System.loadLibrary("core-audiofx")
		}
	}

	init
	{
		if(pointer == null)
		{
			val service = Application.instance.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
			val sampleRate = service.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE).toDouble()
			this.nativePointer = this.constructor(
				BiQuadraticFilter.Type.BELL,
				10000.0,
				0.0,
				0.707,
				sampleRate
			)
		}
		else
		{
			this.nativePointer = pointer
		}
	}

	override fun amplitude(frequency: Double): Double
	{
		return this.amplitude(this.nativePointer, frequency)
	}

	private external fun constructor(
		type: BiQuadraticFilter.Type,
		frequency: Double,
		gain: Double,
		width: Double,
		sampleRate: Double
	) : Long
	private external fun setType(pointer: Long, type: BiQuadraticFilter.Type)
	private external fun getType(pointer: Long): BiQuadraticFilter.Type
	private external fun setFrequency(pointer: Long, frequency: Double)
	private external fun getFrequency(pointer: Long): Double
	private external fun setGain(pointer: Long, gain: Double)
	private external fun getGain(pointer: Long): Double
	private external fun setWidth(pointer: Long, width: Double)
	private external fun getWidth(pointer: Long): Double
	private external fun setEnabled(pointer: Long, state: Boolean)
	private external fun getEnabled(pointer: Long): Boolean
	private external fun amplitude(pointer: Long, frequency: Double): Double
}