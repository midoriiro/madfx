package midoriiro.io.core.extensions

import android.graphics.PointF

fun MutableList<PointF>.addExtrema(minX: Float, maxX: Float): MutableList<PointF>
{
    val minY = this.first().y
    val maxY = this.last().y
    this.add(0, PointF(minX, minY))
    this.add(PointF(maxX, maxY))
    return this
}