package com.oneeyedmen.bowling

@JvmInline
value class Score(val value: Int) {
    operator fun plus(roll: PinCount) = Score(value + roll.value)
    operator fun plus(score: Score) = Score(value + score.value)

    init {
        require(value >= 0)
    }
}