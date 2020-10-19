package midoriiro.io.core.extensions

import android.graphics.Path
import android.graphics.PointF

fun Path.fromPoints(points: List<PointF>)
{
	var isFirst = true
	for(point in points)
	{
		if(isFirst)
		{
			this.moveTo(point.x, point.y)
			isFirst = false
		}
		else
		{
			this.lineTo(point.x, point.y)
		}
	}
}

fun Path.lineToBaseline(points: List<PointF>, baseline: Float)
{
	val minimum = points.minByOrNull { point -> point.x }!!
	val maximum = points.maxByOrNull { point -> point.x }!!
	this.lineTo(maximum.x, baseline)
	this.lineTo(minimum.x, baseline)
}

fun Path.lineToMinimal(points: List<PointF>)
{
	val minimum = points.minByOrNull { point -> point.x }!!
	val maximum = points.maxByOrNull { point -> point.x }!!
	this.lineTo(maximum.x, maximum.y)
	this.lineTo(minimum.x, maximum.y)
}

fun Path.lineToMaximal(points: List<PointF>)
{
	val minimum = points.minByOrNull { point -> point.x }!!
	val maximum = points.maxByOrNull { point -> point.x }!!
	this.lineTo(maximum.x, minimum.y)
	this.lineTo(minimum.x, minimum.y)
}