package sound

import Game
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine
import kotlin.concurrent.thread
import kotlin.math.max
import kotlin.math.min

// https://stackoverflow.com/questions/297070/how-to-generate-sound-effects-in-java

object SoundSystem {
    const val sampleRate = 48000.0
    const val chunkSize = 512
    val format = AudioFormat(
        sampleRate.toFloat(),
        8,
        2,
        true,
        true
    )
    val info = DataLine.Info(
        SourceDataLine::class.java,
        format
    )
    val line = AudioSystem.getLine(info) as SourceDataLine

    fun launch() {
        thread { runThread() }
    }

    private fun runThread() {


        println(format)
        println(info)
        println(line)


        line.open(format)
        line.start()

        while (!Game.shouldClose) {
            line.write(
                generateNextChunk().flatMap {
                    it.toList()
                }.map {
                    max(-128, min(127, (it * 128).toInt())).toByte()
                }.toByteArray(),
                0, chunkSize * 2
            )
            while (line.bufferSize - line.available() > chunkSize * 8) {
                Thread.sleep((1000 * chunkSize / sampleRate).toLong())
            }
        }

        line.stop()
    }

    val gen = ChunkGenMain()
    private fun generateNextChunk() =
        gen.generateNextChunk().also {
            if (it.size != chunkSize)
                throw Error("chunk size mismatch in SoundSystem")
        }

}