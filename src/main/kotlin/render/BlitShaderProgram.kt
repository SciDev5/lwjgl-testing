package render

import util.ResourceLoader

class BlitShaderProgram : ShaderProgram(
     vertexSource = ResourceLoader("/test.vert").contentsOrError,
     fragmentSource = ResourceLoader("/blit.frag").contentsOrError,
) {
     val tex = Uniform<Texture>("tex")
}