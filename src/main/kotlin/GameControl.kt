import fractal.Fractal
import fractal.FractalStepper
import util.ComplexNum
import util.randomSample

object GameControl {
    var currentFractal = Fractal.sfx
        private set
    var fractalStepper = FractalStepper(ComplexNum.zero, ComplexNum.zero, currentFractal)
        private set

    fun fractalSound(x: ComplexNum) {
        fractalStepper = FractalStepper(
            x, x, currentFractal
        )
    }
    fun incrementFractal(n: Int) {
        val farr = arrayOf(
            Fractal.mandelbrot,
            Fractal.burningShip,
            Fractal.shit,
            Fractal.thingy,
            Fractal.shit2,
            Fractal.sfx,
        )
        currentFractal = farr[((currentFractal.shaderIndex + n) % farr.size + farr.size) % farr.size]
        println(currentFractal.shaderIndex)
    }

}