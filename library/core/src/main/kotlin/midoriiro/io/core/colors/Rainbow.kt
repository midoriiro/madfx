package midoriiro.io.core.colors

import midoriiro.io.core.Application
import kotlin.random.Random

class Rainbow
{
	private var _colors: List<Int>

	init
	{
		val context = Application.instance.applicationContext
		val theme = context.theme
		val colors = mutableListOf<Int>()
		for(index in 1..16)
		{
			val id = context.resources.getIdentifier(
				"rainbow$index",
				"color",
				context.packageName
			)

			colors.add(context.resources.getColor(id, theme))
		}
		this._colors = colors
	}

	fun shuffle()
	{
		val even = this._colors.filterIndexed { index, _ -> index % 2 == 0 }
		val odd = this._colors.filterIndexed { index, _ -> index % 2 == 1 }
		this._colors = listOf(even, odd).flatten()
	}

	fun shuffle(seed: Int?)
	{
		if(seed == null)
		{
			this._colors = this._colors.shuffled()
		}
		else
		{
			this._colors = this._colors.shuffled(Random(seed))
		}
	}

	operator fun get(index: Int): Int
	{
		return this._colors[index]
	}
}