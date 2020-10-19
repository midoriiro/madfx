package midoriiro.io.core.components

import android.graphics.RectF
import android.view.View

abstract class Bounds
{
	private val _bounds = RectF()
	private lateinit var _view:View
	private var _paddingTop = 0f
	private var _paddingBottom = 0f
	private var _paddingLeft = 0f
	private var _paddingRight = 0f
	private var _rectangle = RectF()
	private var _relativeRectangle = RectF()
	private var _rectangleWithPadding = RectF()
	private var _relativeRectangleWithPadding = RectF()
	private var _width = 0f
	private var _widthWithPadding = 0f
	private var _halfWidth = 0f
	private var _halfWidthWithPadding = 0f
	private var _height = 0f
	private var _heightWithPadding = 0f
	private var _halfHeight = 0f
	private var _halfHeightWithPadding = 0f
	private var _top = 0f
	private var _topWithPadding = 0f
	private var _bottom = 0f
	private var _bottomWithPadding = 0f
	private var _left = 0f
	private var _leftWithPadding = 0f
	private var _right = 0f
	private var _rightWithPadding = 0f

	val rectangle: RectF
		get() = RectF(this._rectangle)

	val relativeRectangle: RectF
		get() = RectF(this._relativeRectangle)

	val rectangleWithPadding: RectF
		get() = RectF(this._rectangleWithPadding)

	val relativeRectangleWithPadding: RectF
		get() = RectF(this._relativeRectangleWithPadding)

	val width: Float
		get() = this._width

	val widthWithPadding: Float
		get() = this._widthWithPadding

	val halfWidth: Float
		get() = this._halfWidth

	val halfWidthWithPadding: Float
		get() = this._halfWidthWithPadding

	val height: Float
		get() = this._height

	val heightWithPadding: Float
		get() = this._heightWithPadding

	val halfHeight: Float
		get() = this._halfHeight

	val halfHeightWithPadding: Float
		get() = this._halfHeightWithPadding

	val top: Float
		get() = this._top

	val topWithPadding: Float
		get() = this._topWithPadding

	val bottom: Float
		get() = this._bottom

	val bottomWithPadding: Float
		get() = this._bottomWithPadding

	val left: Float
		get() = this._left

	val leftWithPadding: Float
		get() = this._leftWithPadding

	val right: Float
		get() = this._right

	val rightWithPadding: Float
		get() = this._rightWithPadding

	fun toRelative(): RelativeBounds
	{
		val bounds = RelativeBounds()
		bounds.setBounds(
			this._view,
			this._paddingLeft,
			this._paddingTop,
			this._paddingRight,
			this._paddingBottom
		)
		return bounds
	}

	fun toAbsolute(): AbsoluteBounds
	{
		val bounds = AbsoluteBounds()
		bounds.setBounds(
			this._view,
			this._paddingLeft,
			this._paddingTop,
			this._paddingRight,
			this._paddingBottom
		)
		return bounds
	}

	open fun setBounds(
		view: View,
		paddingLeft: Float,
		paddingTop: Float,
		paddingRight: Float,
		paddingBottom: Float
	)
	{
		this._view = view
		this._paddingTop = paddingTop
		this._paddingBottom = paddingBottom
		this._paddingLeft = paddingLeft
		this._paddingRight = paddingRight
	}

	protected fun measure(minX: Float, minY: Float, maxX: Float, maxY: Float)
	{
		this._bounds.set(minX, minY, maxX, maxY)
		this._width = this._bounds.width()
		this._widthWithPadding = this._width - this._paddingRight
		this._halfWidth = this._width / 2f + this._view.paddingLeft / 2f
		this._halfWidthWithPadding = this._widthWithPadding / 2f
		this._height = this._bounds.height()
		this._heightWithPadding = this._height - this._paddingBottom
		this._halfHeight = this._height / 2f + this._view.paddingTop / 2f
		this._halfHeightWithPadding = this._heightWithPadding / 2f
		this._top = this._bounds.top
		this._topWithPadding = this._top + this._paddingTop
		this._bottom = this._bounds.bottom
		this._bottomWithPadding = this._bottom - this._paddingBottom
		this._left = this._bounds.left
		this._leftWithPadding = this._left + this._paddingLeft
		this._right = this._bounds.right
		this._rightWithPadding = this._right - this._paddingRight
		this._rectangle.set(
			this._left,
			this._top,
			this._right,
			this._bottom
		)
		this._relativeRectangle.set(
			this._left,
			this._top,
			this._width,
			this._height
		)
		this._rectangleWithPadding.set(
			this._leftWithPadding,
			this._topWithPadding,
			this._rightWithPadding,
			this._bottomWithPadding
		)
		this._relativeRectangleWithPadding.set(
			this._leftWithPadding,
			this._topWithPadding,
			this._widthWithPadding,
			this._heightWithPadding
		)
	}
}