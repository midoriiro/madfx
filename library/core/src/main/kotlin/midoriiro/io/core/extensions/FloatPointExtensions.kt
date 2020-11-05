package midoriiro.io.core.extensions

import android.graphics.PointF
import androidx.core.graphics.minus
import kotlin.math.sqrt

fun PointF.normalize()
{
    val magnitude = this.magnitude()
    this.x = this.x / magnitude
    this.y = this.y / magnitude
}

fun PointF.normalized(): PointF
{
    val magnitude = this.magnitude()
    return PointF(this.x / magnitude, this.y / magnitude)
}

fun PointF.magnitude(): Float
{
    return sqrt((this.x * this.x) + (this.y * this.y))
}

fun PointF.distance(target: PointF, distance: Float): PointF
{
    if(distance <= 1)
    {
        throw IllegalArgumentException("distance must be greater than 1")
    }
    val d = (target - this).normalized()
    val dx = distance * d.x
    val dy = distance * d.y
    return PointF(this.x + dx, this.y + dy)
}