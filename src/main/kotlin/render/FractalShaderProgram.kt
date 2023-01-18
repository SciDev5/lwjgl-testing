package render

import util.ResourceLoader
import util.Vec2

class FractalShaderProgram : ShaderProgram(
     Source.vert.contentsOrError,
     Source.frag.contentsOrError,
) {
    object Source {
        val frag = ResourceLoader("/test.frag")
        val vert = ResourceLoader("/test.vert")
    }


    val center = Uniform<Vec2>("center")
    val scale = Uniform<Float>("scale")
    val aspect = Uniform<Float>("aspect")
    val enableColor = Uniform<Boolean>("enableColor")

    val fractalIndex = Uniform<Int>("fractalIndex")


    val colorOut = Uniform<RenderTexture>("color")
}