package sound

abstract class ChunkGen {
    abstract fun generateNextChunk(): List<Pair<Double, Double>>
}