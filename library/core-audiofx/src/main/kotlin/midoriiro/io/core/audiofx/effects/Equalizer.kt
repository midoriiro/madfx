package midoriiro.io.core.audiofx.effects

class Equalizer
{
	private val _nativePointer: Long

	companion object {
		init {
			System.loadLibrary("core-audiofx")
		}
	}

	init
	{
		this._nativePointer = this.constructor()
	}

	fun create(): BiQuadraticFilter
	{
		return BiQuadraticFilterNative(null)
	}

	fun add(filter: BiQuadraticFilter): Boolean
	{
		if(this.size(this._nativePointer) == 16)
		{
			return false
		}
		return this.add(this._nativePointer, (filter as BiQuadraticFilterNative).nativePointer)
	}

	fun remove(filter: BiQuadraticFilter): Boolean
	{
		return this.remove(this._nativePointer, (filter as BiQuadraticFilterNative).nativePointer)
	}

	fun get(index: Int): BiQuadraticFilter?
	{
		if(!(0 <= index && index <= this.size(this._nativePointer) - 1))
		{
			return null;
		}
		val nativePointer = this.get(this._nativePointer, index)
		return BiQuadraticFilterNative(nativePointer)
	}

	fun update()
	{
		this.update(this._nativePointer)
	}

	private external fun constructor() : Long
	private external fun add(pointer: Long, filter: Long): Boolean
	private external fun remove(pointer: Long, filter: Long): Boolean
	private external fun get(pointer: Long, index: Int): Long
	private external fun update(pointer: Long)
	private external fun size(pointer: Long): Int
}