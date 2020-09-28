package midoriiro.madfx.audio.equalizers

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


/**
 * Based on:
 *  - https://webaudio.github.io/Audio-EQ-Cookbook/audio-eq-cookbook.html
 *  - https://www.diva-portal.org/smash/get/diva2:1031081/FULLTEXT01.pdf
 *  - https://arachnoid.com/BiQuadDesigner/index.html
 */
class BiQuadraticFilter
{
	enum class Type
	{
		LOW_PASS,
		HIGH_PASS,
		BAND_PASS,
		BELL,
		NOTCH,
		LOW_SHELF,
		HIGH_SHELF
	}
	
	private companion object
	{
		const val Q_MINIMUM = 0.025
		const val Q_MAXIMUM = 30.0
		const val GAIN_MINIMUM = -30.0
		const val GAIN_MAXIMUM = 30.0
		const val FREQUENCY_MINIMUM = 10.0
		const val FREQUENCY_MAXIMUM = 30000.0
	}
	
	private var _x1: Double = 0.0
	private var _x2: Double = 0.0
	private var _y: Double = 0.0
	private var _y1: Double = 0.0
	private var _y2: Double = 0.0
	
	private var _a0: Double = 0.0
	private var _a1: Double = 0.0
	private var _a2: Double = 0.0
	private var _b0: Double = 0.0
	private var _b1: Double = 0.0
	private var _b2: Double = 0.0
	
	private var _fs: Double = 0.0
	private var _f: Double = 0.0
	private var _g: Double = 0.0
	private var _q: Double = 0.0
	private var _type: Type = Type.BELL
	
	private var frequency: Double
		set(value)
		{
			this._f = value
			
			if(this._f < FREQUENCY_MINIMUM)
			{
				this._f = FREQUENCY_MINIMUM
			}
			else if(this._f > FREQUENCY_MAXIMUM)
			{
				this._f = FREQUENCY_MAXIMUM
			}
			
			this._f = this._f / this._fs
		}
		get() = this._f
	
	private var gain: Double
		set(value)
		{
			this._g = value
			
			if(this._g < GAIN_MINIMUM)
			{
				this._g = GAIN_MINIMUM
			}
			else if(this._g > GAIN_MAXIMUM)
			{
				this._g = GAIN_MAXIMUM
			}
		}
		get() = this._g
	
	private var width: Double
		set(value)
		{
			this._q = value
			
			if(this._q < Q_MINIMUM)
			{
				this._q = Q_MINIMUM
			}
			else if(this._q > Q_MAXIMUM)
			{
				this._q = Q_MAXIMUM
			}
		}
		get() = this._q
	
	constructor(type: Type, frequency: Double, gain: Double,  width: Double, rate: Double)
	{
		this.configure(type, frequency, gain, width, rate)
	}
	
	fun configure(type: Type, frequency: Double, gain: Double,  width: Double, rate: Double)
	{
		this.reset()
		this._type = type
		this._fs = rate
		this.frequency = frequency
		this.gain = gain
		this.width = width
		this.computeConstants()
	}
	
	fun evaluate(frequency: Double): Double
	{
		val phi  = (sin(2.0 * PI * frequency / (2.0 * this._fs))).pow(2.0)
		val phi2 = phi * phi
		val a2 = (this._a0 + this._a1 + this._a2).pow(2.0)
		val b2 = (this._b0 + this._b1 + this._b2).pow(2.0)
		val a4 = this._a0*this._a1 + 4.0*this._a0*this._a2 + this._a1*this._a2
		val b4 = this._b0*this._b1 + 4.0*this._b0*this._b2 + this._b1*this._b2
		val numerator = b2 - 4.0*(b4)*phi + 16.0*this._b0*this._b2*phi2
		val denominator = a2 - 4.0*(a4)*phi + 16.0*this._a0*this._a2*phi2
		return 20.0 * log10(sqrt(numerator / denominator))
	}
	
	fun filter(frequency: Double): Double
	{
		this._y = (this._b0 * frequency) + (this._b1 * this._x1) + (this._b2 * this._x2) - (this._a1 * this._y1) - (this._a2 * this._y2)
		this._x2 = this._x1
		this._x1 = frequency
		this._y2 = this._y1
		this._y1 = this._y
		return this._y
	}
	
	private fun reset()
	{
		this._x1 = 0.0
		this._x2 = 0.0
		this._y1 = 0.0
		this._y2 = 0.0
	}
	
	private fun computeConstants()
	{
		val A = (this._g / 40.0).pow(10.0)
		val omega = 2 * PI * this._f / this._fs
		val sin = sin(omega)
		val cos = cos(omega)
		val alpha = sin / (2.0 * this._q)
		val beta = sqrt(A + A)
		when(this._type)
		{
			Type.BAND_PASS -> {
				this._b0 = alpha
				this._b1 = 0.0
				this._b2 = -alpha
				this._a0 = +1.0 + alpha
				this._a1 = -2.0 * cos
				this._a2 = +1.0 - alpha
			}
			Type.LOW_PASS -> {
				val cosm = 1.0 - cos
				val cosmd = cosm / 2.0
				this._b0 = cosmd
				this._b1 = cosm
				this._b2 = cosmd
				this._a0 = +1.0 + alpha
				this._a1 = -2.0 * cos
				this._a2 = +1.0 - alpha
			}
			Type.HIGH_PASS -> {
				val cosp = 1.0 + cos
				val cospd = cosp / 2.0
				this._b0 = cospd
				this._b1 = -cosp
				this._b2 = cospd
				this._a0 = +1.0 + alpha
				this._a1 = -2.0 * cos
				this._a2 = +1.0 - alpha
			}
			Type.NOTCH -> {
				val cosm = -2.0 * cos
				this._b0 = +1.0
				this._b1 = cosm
				this._b2 = +1.0
				this._a0 = +1.0 + alpha
				this._a1 = cosm
				this._a2 = +1.0 - alpha
			}
			Type.BELL -> {
				val am = alpha * A
				val ad = alpha / A
				val cosm = -2.0 * cos
				this._b0 = +1.0 + am
				this._b1 = cosm
				this._b2 = +1.0 - am
				this._a0 = +1.0 + ad
				this._a1 = cosm
				this._a2 = +1.0 - ad
			}
			Type.LOW_SHELF -> {
				val ap = A + 1.0
				val am = A - 1.0
				this._b0 = A * (ap - am * cos + beta * sin)
				this._b1 = +2.0 * A * (am - ap * cos)
				this._b2 = A * (ap - am * cos - beta * sin)
				this._a0 = ap + am * cos + beta * sin
				this._a1 = -2.0 * (am + ap * cos)
				this._a2 = ap + am * cos - beta * sin
			}
			Type.HIGH_SHELF -> {
				val ap = A + 1.0
				val am = A - 1.0
				this._b0 = A * (ap + am * cos + beta * sin)
				this._b1 = -2.0 * A * (am + ap * cos)
				this._b2 = A * (ap + am * cos - beta * sin)
				this._a0 = ap - ap * cos + beta * sin
				this._a1 = 2.0 * (am - ap * cos)
				this._a2 = ap - am * cos - beta * sin
			}
		}
		this._b0 /= this._a0
		this._b1 /= this._a0
		this._b2 /= this._a0
		this._a1 /= this._a0
		this._a2 /= this._a0
		this._a0 = 1.0
	}
}