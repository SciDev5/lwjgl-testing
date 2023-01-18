import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import sound.SoundSystem
import util.Vec2
import java.awt.DisplayMode
import javax.swing.Spring.height


object Game {
    /** The window handle */
    var window: Long = 0
        private set


    object WindowConfig {
        const val borderless = false
        const val fullscreen = false
        const val resizable = true
        const val transparent = true

        const val startWidth = 1000
        const val startHeight = 600
    }
    private val Boolean.glfwConst get() = if (this) GLFW_TRUE else GLFW_FALSE


    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!")
        init()
        SoundSystem.launch()
        loop()

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)

        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }


    private fun init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        check(glfwInit()) { "Unable to initialize GLFW" }

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

        glfwWindowHint(GLFW_RESIZABLE, WindowConfig.resizable.glfwConst)
        internalBorderless = WindowConfig.borderless
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, WindowConfig.transparent.glfwConst)

        // Create the window
        window = glfwCreateWindow(
            WindowConfig.startWidth,
            WindowConfig.startHeight,
            "LWJGL Testing",
            NULL, NULL
        )
        if (window == NULL) throw Error("Failed to create the GLFW window")

        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window) { _: Long, key: Int, _: Int, action: Int, mods: Int ->
            when (action) {
                GLFW_PRESS -> GameEvents.keyPress(key, mods)
                GLFW_RELEASE -> GameEvents.keyRelease(key, mods)
            }
        }
        glfwSetCursorPosCallback(window) { _: Long, x: Double, y: Double ->
            GameEvents.mouseMove(Vec2(x,y))
        }
        glfwSetMouseButtonCallback(window) { _: Long, button: Int, action: Int, mods: Int ->
            when (action) {
                GLFW_PRESS -> GameEvents.mousePress(button, mods)
                GLFW_RELEASE -> GameEvents.mouseRelease(button, mods)
            }
        }
        glfwSetScrollCallback(window) { _: Long, dx: Double, dy: Double ->
            GameEvents.scroll(Vec2(dx,dy))
        }


        // Get the resolution of the primary monitor
        val glfwVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!

        // Center the window
        val (windowWidth, windowHeight) = readWindowSize()
        glfwSetWindowPos(
            window,
            (glfwVidMode.width() - windowWidth) / 2,
            (glfwVidMode.height() - windowHeight) / 2
        )
        internalFullscreen = WindowConfig.fullscreen


        // Make the OpenGL context current
        glfwMakeContextCurrent(window)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window)
    }


    private fun loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        // Game init event
        GameEvents.init()

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!shouldClose) {

            // Game frame event
            GameEvents.frame()

            // swap the color buffers, shows what was rendered
            glfwSwapBuffers(window)

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()

            // Refresh window information
            updateData()
        }

        // Game cleanup event
        GameEvents.cleanup()
    }
    private fun updateData() {
        readWindowSize()

        shouldClose = glfwWindowShouldClose(window)
    }




    var windowSize = Pair(0, 0)
        private set
    private fun readWindowSize() =
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            windowSize = Pair(pWidth[0], pHeight[0])
            windowSize
        }
    fun closeWindow() {
        glfwSetWindowShouldClose(
            window,
            true
        )
    }
    var shouldClose = false
        private set

    var borderless = WindowConfig.borderless
        set(value) {
            field = value
            internalBorderless = value || fullscreen
        }
    var fullscreen = WindowConfig.fullscreen
        set(value) {
            field = value
            internalBorderless = borderless || value
            internalFullscreen = value
        }

    private var internalBorderless = false
        set(value) {
            if (field == value) return
            field = value

            glfwSetWindowAttrib(window, GLFW_DECORATED, if (value) GLFW_FALSE else GLFW_TRUE)
        }
    private var internalFullscreen = false
        set(value) {
            if (field == value) return
            field = value

            if (value)
                glfwMaximizeWindow(window)
            else
                glfwRestoreWindow(window)
    }
}