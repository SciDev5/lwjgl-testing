import org.lwjgl.opengl.GL13.*
import render.*
import util.Vec2

object Render {
    private var shader = FractalShaderProgram()
    private var colorShader = LineShaderProgram()
    private var blitShader = BlitShaderProgram()
    private val mesh = Mesh2D.build {
        val v = arrayOf(
            Vec2(-1, -1),
            Vec2(1, -1),
            Vec2(-1, 1),
            Vec2(1, 1),
        )
        tri(v[1], v[2], v[0])
        tri(v[1], v[2], v[3])
    }
    private val testLine = Mesh2D.build {
        vert(0.0,0.0)
        vert(1.0,0.0)
        vert(1.0,1.0)
    }

    fun updateShader() {
        println("RELOADED SHADER")
        shader.cleanUp()
        shader = FractalShaderProgram()
    }

    fun init() {
        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        updateShader()
    }

    var scale = 1.0
    var center = Vec2.zero
    var enableColor = false

    val rt = RenderTexture(Vec2.fromI(Game.windowSize))

    fun frame(dt: Double) {
        val (width, height) = Game.windowSize
        glViewport(0, 0, width, height)
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT + GL_DEPTH_BUFFER_BIT)

        val pathMesh = GameControl.fractalStepper.mesh

        if ((rt.dim - Vec2(width,height)).mag > .5) {
            rt.dim = Vec2(width, height)
        }
//        rt.renderTo {
//            glViewport(0, 0, width, height)
//            glClearColor(0.5f, 0.0f, 0.0f, 0.5f)
//            glClear(GL_COLOR_BUFFER_BIT)
            shader.use {
                shader.aspect.assign(height / width.toDouble())
                shader.center.assign(center)
                shader.scale.assign(scale)
                shader.enableColor.assign(enableColor)
                shader.fractalIndex.assign(GameControl.currentFractal.shaderIndex)
                shader.colorOut.assign(rt,0)
                mesh.applyAttrib(shader, "pos")
                glDrawArrays(GL_TRIANGLES, 0, 6)
            }
//        }
//        blitShader.use {
//            blitShader.tex.assign(rt,0)
//            mesh.applyAttrib(shader, "pos")
//            glDrawArrays(GL_TRIANGLES, 0, 3)
//        }

        colorShader.use {
            colorShader.aspect.assign(height / width.toDouble())
            colorShader.center.assign(center)
            colorShader.scale.assign(scale)
            pathMesh.applyAttrib(shader, "pos")

            glDrawArrays(GL_LINE_STRIP, 0, pathMesh.verts.size)
        }

    }

    fun cleanup() {
        shader.cleanUp()
    }

}