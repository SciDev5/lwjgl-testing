package fractal

import util.ComplexNum
import util.abs
import util.exp

class Fractal(
    val shaderIndex: Int,
    val stepFn: (z: ComplexNum, c: ComplexNum) -> ComplexNum
) {

    companion object {
        val mandelbrot = Fractal(0) { z, c ->
            z * z + c
        }
        val burningShip = Fractal(1) { z, c ->
            abs(z).squared + c
        }
        val shit = Fractal(2) { z, c ->
            z.squared + abs(c - z)
        }
        val thingy = Fractal(3) { z, c ->
            z * (z + c) + z * c
        }
        val shit2 = Fractal(4) { z, c ->
            z.squared*z + abs(c-z) * exp(z)
        }
        val sfx = Fractal(5) { z, c ->
            z * (z.r*z.r+z.i*z.i) - (z * (c.toVec()*c.toVec()).toComplex())
        }


    }
}