package util

import kotlin.random.Random

fun <T> Array<T>.randomSample(): T
    = this[Random.nextInt(size)]