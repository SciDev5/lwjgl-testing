package util

import kotlin.math.*

class ComplexNum(
    val r: Double,
    val i: Double
) {
    constructor(x:Int,y:Int) : this(x.toDouble(),y.toDouble())

    operator fun plus(other: ComplexNum) = ComplexNum(this.r + other.r, this.i + other.i)
    operator fun minus(other: ComplexNum) = this + -other
    operator fun unaryMinus() = this * -1.0
    operator fun times(other: ComplexNum) = ComplexNum(
        this.r * other.r - this.i * other.i,
        this.r * other.i + this.i * other.r
    )
    operator fun times(other: Double) = ComplexNum(this.r * other, this.i * other)
    operator fun div(other: Double) = this * (1.0 / other)

    val length get() = sqrt(r*r+i*i)
    val squared get() = this * this

    operator fun component1() = r
    operator fun component2() = i

    companion object {
        val zero = ComplexNum(0.0, 0.0)
        val one = ComplexNum(1.0, 1.0)
    }

    fun toVec() = Vec2(r,i)
}

fun abs(v: ComplexNum) = ComplexNum(abs(v.r),abs(v.i))
fun exp(v: ComplexNum) = ComplexNum(cos(v.i),sin(v.i)) * exp(v.r)