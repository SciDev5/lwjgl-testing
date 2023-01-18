package render

import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL32.*
import util.Vec2

class RenderTexture(
    dimIn: Vec2

) : Texture {
    var dim = dimIn
        set(value) {
            field = value
            clear()
        }

    override val texId: Int = glGenTextures()

    init {

        glBindTexture(GL_TEXTURE_2D, texId)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)

        clear()

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texId, 0)
        glDrawBuffers(GL_COLOR_ATTACHMENT0)

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw Error("RenderTexture setup failed")
    }

    fun clear() {
        glBindTexture(GL_TEXTURE_2D, texId)
        glDrawBuffers(GL_COLOR_ATTACHMENT0)
        println("${dim.x.toInt()}, ${dim.y.toInt()}")
        glTexImage2D(
            GL_TEXTURE_2D,
            0, GL_RGBA32F,
            dim.x.toInt(), dim.y.toInt(),
            0,
            GL_RGBA,
            GL_FLOAT,
            0
        )
    }

    fun renderTo(block: ()->Unit) {
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, texId);
        block()
        glBindFramebuffer(GL_FRAMEBUFFER, 0) // switch back to window
    }
}