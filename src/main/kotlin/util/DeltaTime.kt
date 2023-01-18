package util

class DeltaTime {
    private val tStart = System.nanoTime()
    private var tLast = tStart

    fun dt():Double {
        val t = System.nanoTime()
        val dt = (t - tLast) * 1e-6 // nanos to millis
        tLast = t
        return dt
    }

    val elapsedTotal get() = System.nanoTime() - tStart
}