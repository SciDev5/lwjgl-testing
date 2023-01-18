import org.lwjgl.glfw.GLFW
import util.DeltaTime
import util.Vec2
import kotlin.math.pow

object GameEvents {

    private val deltaTimer = DeltaTime()

    fun init() {
        Render.init()
    }

    fun frame() = frame(deltaTimer.dt())
    private fun frame(dt: Double) {
        Render.frame(dt)
    }

    fun cleanup() {
        Render.cleanup()
    }

    private var leftMouseHeld = false
    private var soundButtonHeld = false
    private var cHeldTimeout = 0L
    private var mousePos = Vec2.zero

    private val screenToRealScale
        get() = (2.0 / Game.windowSize.second * Render.scale)
    private val mousePosScaled
        get() = (mousePos - Vec2(Game.windowSize.first, Game.windowSize.second) / 2.0) * screenToRealScale


    fun mouseMove(p: Vec2) {
        val dp = p - mousePos
        mousePos = p
        if (leftMouseHeld) {
            Render.center += dp * screenToRealScale
        }
        if (soundButtonHeld && System.currentTimeMillis() > cHeldTimeout) {
            val x = (mousePosScaled - Render.center).toComplex()
            GameControl.fractalSound(x)
            cHeldTimeout = System.currentTimeMillis() + 50
        }
    }

    fun scroll(d: Vec2) {
        val realMousePos = mousePosScaled
        val scaleFac = 2.0.pow(d.y * .1)
        Render.scale *= scaleFac
        Render.center -= realMousePos * (1 - scaleFac)
    }

    fun mousePress(button: Int, mods: Int) {
        when (button) {
            GLFW.GLFW_MOUSE_BUTTON_1 -> leftMouseHeld = true
        }
    }

    fun mouseRelease(button: Int, mods: Int) {
        when (button) {
            GLFW.GLFW_MOUSE_BUTTON_1 -> leftMouseHeld = false
        }
    }

    fun keyPress(key: Int, mods: Int) {
        when (key) {
            GLFW.GLFW_KEY_R -> {
                if (mods and GLFW.GLFW_MOD_CONTROL != 0)
                    Render.updateShader()
            }

            GLFW.GLFW_KEY_SPACE -> {
                val x = (mousePosScaled - Render.center).toComplex()
                GameControl.fractalSound(x)

                soundButtonHeld = true
            }
            GLFW.GLFW_KEY_PERIOD -> GameControl.incrementFractal(1)
            GLFW.GLFW_KEY_COMMA -> GameControl.incrementFractal(-1)

            GLFW.GLFW_KEY_B -> {
                Game.borderless = !Game.borderless
            }
            GLFW.GLFW_KEY_F -> {
                Game.fullscreen = !Game.fullscreen
            }

            GLFW.GLFW_KEY_C -> {
                Render.enableColor = !Render.enableColor
            }
        }
    }

    fun keyRelease(key: Int, mods: Int) {
        when (key) {
            GLFW.GLFW_KEY_ESCAPE -> {
                if (mods and GLFW.GLFW_MOD_SHIFT > 0) Game.closeWindow()
            }
            GLFW.GLFW_KEY_SPACE -> {
                soundButtonHeld = false
            }
        }
    }
}