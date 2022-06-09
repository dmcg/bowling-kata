package com.oneeyedmen.bowling

typealias Player = String

@JvmInline
value class PinCount(val value: Int) {
    operator fun plus(roll2: PinCount) = Score(value + roll2.value)

    init {
        require(value in 0..10)
    }

    fun asScore() = Score(value)
}

@JvmInline
value class Score(val value: Int) {
    operator fun plus(roll: PinCount) = Score(value + roll.value)
    operator fun plus(score: Score) = Score(value + score.value)

    init {
        require(value >= 0)
    }
}

fun <E> Iterable<E>.replacing(old: E, new: E) =
    map {
        if (it == old) new else it
    }
