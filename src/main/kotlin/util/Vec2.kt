package util

import kotlin.math.sqrt

class Vec2(
    val x: Double,
    val y: Double
) {
    constructor(x:Int,y:Int) : this(x.toDouble(),y.toDouble())

    operator fun plus(other: Vec2) = Vec2(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vec2) = this + -other
    operator fun unaryMinus() = this * -1.0
    operator fun times(other: Vec2) = Vec2(this.x * other.x, this.y * other.y)
    operator fun times(other: Double) = Vec2(this.x * other, this.y * other)
    operator fun div(other: Vec2) = Vec2(this.x / other.x, this.y / other.y)
    operator fun div(other: Double) = this * (1.0 / other)

    operator fun component1() = x.toFloat()
    operator fun component2() = y.toFloat()

    val mag get() = sqrt(x*x+y*y)

    companion object {
        val zero = Vec2(0.0, 0.0)
        val one = Vec2(1.0, 1.0)

        fun fromD(pair:Pair<Double,Double>) = Vec2(pair.first,pair.second)
        fun fromI(pair:Pair<Int,Int>) = Vec2(pair.first,pair.second)
    }

    fun toComplex() = ComplexNum(x,y)
}