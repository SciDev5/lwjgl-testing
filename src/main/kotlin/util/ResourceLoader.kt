package util

class ResourceLoader(private val loc: String) {
    val contents
        get() = ResourceLoader::class.java.getResource(loc)?.readText()
    val contentsOrError
        get() = contents ?: throw Error("Resource missing: '$loc'")
}