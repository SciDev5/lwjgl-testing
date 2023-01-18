package render

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import util.Vec2

class Mesh2D(
    vararg val verts: Vec2
) {

    class Builder {
        val verts = mutableListOf<Vec2>()

        inner class BuildScope {
            fun tri(
                v0: Vec2, v1: Vec2, v2: Vec2
            ) {
                verts.addAll(arrayOf(v0, v1, v2))
            }
            fun vert(
                x: Double, y: Double
            ) {
                verts.add(Vec2(x,y))
            }
        }
    }

    companion object {
        fun build(block: Builder.BuildScope.() -> Unit) = Mesh2D(
            *Builder().also {
                block(it.BuildScope())
            }.verts.toTypedArray()
        )
    }


    fun applyAttrib(program: ShaderProgram, attribName: String) {
        val floatBuff = BufferUtils.createFloatBuffer(verts.size*2)
        for (i in verts.indices) {
            val (x,y) = verts[i]
            floatBuff.put(i*2, x)
            floatBuff.put(i*2+1, y)
        }

        val loc = glGetAttribLocation(program.programID,attribName)

        glVertexAttribPointer(loc,2, GL_FLOAT,false,0,floatBuff)
        glEnableVertexAttribArray(loc)
    }
}