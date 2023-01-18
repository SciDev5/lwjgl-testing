package sound

class ChunkGenMain : ChunkGen() {
    private val fac = 0.001
    private var rollingAvg = Pair(0.0,0.0)
    private fun highpass(k: Pair<Double,Double>):Pair<Double,Double> {
        val (r0,r1) = rollingAvg
        val (k0,k1) = k
        rollingAvg = Pair(
            r0*(1-fac) + k0 * fac,
            r1*(1-fac) + k1 * fac
        )
        return Pair(
            k0 - r0,
            k1 - r1
        )
    }

    override fun generateNextChunk(): List<Pair<Double, Double>> {
        return FractalSoundChunkGen.generateNextChunk().map(this::highpass)
    }
}