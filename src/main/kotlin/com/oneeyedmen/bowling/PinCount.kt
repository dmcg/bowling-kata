package com.oneeyedmen.bowling

@JvmInline
value class PinCount(val value: Int) {
    operator fun plus(roll2: PinCount) = Score(value + roll2.value)

    init {
        require(value in 0..10)
    }

    fun asScore() = Score(value)
}