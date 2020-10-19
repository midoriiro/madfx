package midoriiro.madfx.audio.equalizers

import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import midoriiro.madfx.R
import midoriiro.io.core.components.RelativeBounds
import midoriiro.io.core.extensions.fromDp

class EqualizerControlPanel : View
{
	private val _painter = Paint(Paint.ANTI_ALIAS_FLAG)
	private val _bounds = RelativeBounds()

	constructor(
		context: Context, attrs: AttributeSet
	) : super(context, attrs)
	{
		this.init(attrs, 0)
	}

	constructor(
		context: Context, attrs: AttributeSet, defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.init(attrs, defStyleAttr)
	}

	constructor(
		context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.init(attrs, defStyleAttr)
	}

	private fun init(attrs: AttributeSet, defStyle: Int)
	{

	}

	override fun onSizeChanged(
		w: Int,
		h: Int,
		oldw: Int,
		oldh: Int
	)
	{
		super.onSizeChanged(w, h, oldw, oldh)
		this._bounds.setBounds(
			this,
			10f,
			10f,
			10f,
			10f
		)
	}

	override fun onDraw(canvas: Canvas)
	{
		val path = Path()
		path.moveTo(this._bounds.leftWithPadding, this._bounds.topWithPadding)
		path.lineTo(this._bounds.halfWidth, this._bounds.top)
		path.lineTo(this._bounds.widthWithPadding, this._bounds.topWithPadding)
		path.lineTo(this._bounds.width, this._bounds.halfHeight)
		path.lineTo(this._bounds.widthWithPadding, this._bounds.heightWithPadding)
		path.lineTo(this._bounds.halfWidth, this._bounds.height)
		path.lineTo(this._bounds.leftWithPadding, this._bounds.heightWithPadding)
		path.lineTo(this._bounds.left, this._bounds.halfHeight)
		path.close()
		this._painter.style = Paint.Style.STROKE
		this._painter.color = this.context.getColor(R.color.material_on_surface_disabled)
		this._painter.strokeWidth = 2f.fromDp()
		this._painter.pathEffect = CornerPathEffect(10f)
		canvas.drawPath(path, this._painter)
	}
}