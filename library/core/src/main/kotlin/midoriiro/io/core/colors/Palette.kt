package midoriiro.io.core.colors

import android.graphics.Paint
import android.view.View
import midoriiro.io.core.Application
import midoriiro.io.core.R
import midoriiro.io.core.extensions.*

class Palette(view: View)
{
	private val _view = view
	private val _primary: Int
	private val _primaryDark: Int
	private val _secondary: Int
	private val _secondaryDark: Int
	private val _background: Int
	private val _surface: Int
	private val _error: Int
	private val _onPrimary: Int
	private val _onSecondary: Int
	private val _onBackground: Int
	private val _onSurface: Int
	private val _onError: Int

	val primary: Int
		get() = this._primary

	val primaryDark: Int
		get() = this._primaryDark

	val secondary: Int
		get() = this._secondary

	val secondaryDark: Int
		get() = this._secondaryDark

	val background: Int
		get() = this._background

	val surface: Int
		get() = this._surface

	val error: Int
		get() = this._error

	val onPrimary: Int
		get() = this._onPrimary

	val onSecondary: Int
		get() = this._onSecondary

	val onBackground: Int
		get() = this._onBackground

	val onSurface: Int
		get() = this._onSurface

	val onError: Int
		get() = this._onError

	init
	{
		this._primary = this.getColor(R.color.primary)
		this._primaryDark = this.getColor(R.color.primaryDark)
		this._secondary = this.getColor(R.color.secondary)
		this._secondaryDark = this.getColor(R.color.secondaryDark)
		this._background = this.getColor(R.color.background)
		this._surface = this.getColor(R.color.surface)
		this._error = this.getColor(R.color.error)
		this._onPrimary = this.getColor(R.color.onPrimary)
		this._onSecondary = this.getColor(R.color.onSecondary)
		this._onBackground = this.getColor(R.color.onBackground)
		this._onSurface = this.getColor(R.color.onSurface)
		this._onError = this.getColor(R.color.onError)
	}

	private fun getColor(id: Int): Int
	{
		return Application.instance.getColor(id)
	}

	// get color for text based on state
	// get color for fill shape based on state
	// get color for stroke shape based on state

	fun getTextColorFromState(color: Int): Int
	{
		return if(this._view.isEnabled)
		{
			color
		}
		else
		{
			color.toLowEmphasis()
		}
	}

	fun getOverlayColorFromState(color: Int): Int
	{
		return when
		{
			!this._view.isEnabled ->
			{
				color.toDisabledState()
			}
			this._view.isHovered ->
			{
				color.toOverlayHovered()
			}
			this._view.isFocused ->
			{
				color.toOverlayFocused()
			}
			this._view.isPressed ->
			{
				color.toOverlayPressed()
			}
			else -> color
		}
	}

	fun getStrokeColorFromState(color: Int): Int
	{
		return when
		{
			!this._view.isEnabled ->
			{
				this._onSurface.toDisabledState()
			}
			this._view.isFocused ->
			{
				color.toStrokeFocused()
			}
			else ->
			{
				color
			}
		}
	}
}