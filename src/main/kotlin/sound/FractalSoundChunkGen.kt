package sound

import GameControl
import util.ComplexNum
import util.Vec2

object FractalSoundChunkGen : ChunkGen() {
    private const val subChunkLen = 64

    override fun generateNextChunk(): List<Pair<Double, Double>> {
        val stepper = GameControl.fractalStepper

        return arrayOfNulls<Any>(SoundSystem.chunkSize / subChunkLen).flatMap {
            stepper.step()
            arrayOfNulls<Any>(subChunkLen).map {
                val (r, i) = stepper.z / 2.0
                Pair(r, i)
            }
        }
    }
}