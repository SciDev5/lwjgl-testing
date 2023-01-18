package render

import util.Vec2
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

 abstract class ShaderProgram(vertexSource: String, fragmentSource: String) {
    val programID: Int
    private val vertexShaderID: Int
    private val fragmentShaderID: Int

    init {
        vertexShaderID = loadShader(vertexSource, GL20.GL_VERTEX_SHADER)
        fragmentShaderID = loadShader(fragmentSource, GL20.GL_FRAGMENT_SHADER)
        programID = GL20.glCreateProgram()
        GL20.glAttachShader(programID, vertexShaderID)
        GL20.glAttachShader(programID, fragmentShaderID)
        GL20.glLinkProgram(programID)
        GL20.glValidateProgram(programID)
    }

    fun use(block: ()->Unit) {
        GL20.glUseProgram(programID)
        block()
        GL20.glUseProgram(0)
    }

    fun cleanUp() {
        GL20.glDetachShader(programID, vertexShaderID)
        GL20.glDetachShader(programID, fragmentShaderID)
        GL20.glDeleteShader(vertexShaderID)
        GL20.glDeleteShader(fragmentShaderID)
        GL20.glDeleteProgram(programID)
    }

    private fun loadShader(shaderSource: String, type: Int): Int {
        val shaderID = GL20.glCreateShader(type)
        GL20.glShaderSource(shaderID, shaderSource)
        GL20.glCompileShader(shaderID)
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            println(GL20.glGetShaderInfoLog(shaderID, 500))
//            throw Error("Could not compile shader.")
        }
        return shaderID
    }


    fun setUniformVec2(name: String, v: Vec2) {
        val (x,y) = v
        val loc = GL20.glGetUniformLocation(programID, name)
        GL20.glUniform2f(loc,x,y)
    }
    fun setUniformFloat(name: String, v: Double)
        = setUniformFloat(name, v.toFloat())
    fun setUniformFloat(name: String, v: Float) {
        val loc = GL20.glGetUniformLocation(programID, name)
        GL20.glUniform1f(loc,v)
    }

    inner class Uniform<T>(name: String) {
        val loc = GL20.glGetUniformLocation(programID, name)
    }
}
fun ShaderProgram.Uniform<Vec2>.assign(v: Vec2) {
    val (x,y) = v
    GL20.glUniform2f(loc, x, y)
}
fun ShaderProgram.Uniform<Float>.assign(v: Float) {
    GL20.glUniform1f(loc, v)
}
fun ShaderProgram.Uniform<Float>.assign(v: Double) {
    GL20.glUniform1f(loc, v.toFloat())
}
fun ShaderProgram.Uniform<Int>.assign(v: Int) {
    GL20.glUniform1i(loc, v)
}
fun ShaderProgram.Uniform<Boolean>.assign(v: Boolean) {
    GL20.glUniform1i(loc, if (v) 1 else 0)
}
fun <T : Texture> ShaderProgram.Uniform<T>.assign(v: T, n: Int) {
    GL20.glActiveTexture(intArrayOf(
        GL20.GL_TEXTURE0,
        GL20.GL_TEXTURE1,
        GL20.GL_TEXTURE2,
        GL20.GL_TEXTURE3,
        GL20.GL_TEXTURE4
    )[n])
    GL20.glBindTexture(GL20.GL_TEXTURE_2D, v.texId)
    GL20.glUniform1i(loc, n)
}