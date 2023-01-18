package fractal

import render.Mesh2D
import util.ComplexNum
import util.Vec2

class FractalStepper(
    z0: ComplexNum,
    private val c: ComplexNum,
    private val fractal: Fractal
) {
    private val list = mutableListOf(z0.toVec())
    val mesh get() = Mesh2D(*list.toTypedArray())

    var z = z0
        private set

    fun step() {
        if (ended) return
        z = fractal.stepFn(z, c)
        if (list.size < 1000)
            list.add(z.toVec())
    }

    val ended get() = z.length > 1e3
}
