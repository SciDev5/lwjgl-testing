package render

import util.ResourceLoader
import util.Vec2

class LineShaderProgram : ShaderProgram(
     vertexSource = ResourceLoader("/line.vert").contentsOrError,
     fragmentSource = """
void main() {
    gl_FragColor = vec4(1.0,0.0,0.0,1.0);
}
     """.trimIndent(),
) {

     val center = Uniform<Vec2>("center")
     val scale = Uniform<Float>("scale")
     val aspect = Uniform<Float>("aspect")
}