package com.oneeyedmen.bowling

@JvmInline
value class PinCount(
    val value: Int
) {
    operator fun plus(roll2: PinCount) = Score(value + roll2.value)

    init {
        require(value in 0..10)
    }
}

@JvmInline
value class Score(
    val value: Int
) {
    operator fun plus(pinCount: PinCount) = Score(value + pinCount.value)

    operator fun plus(other: Score) = Score(value + other.value)

    init {
        require(value in 0..300)
    }
}

typealias Player = String

typealias Scorecard = List<String>

fun newGame(frames: Int): Game = Game(frames = 0.until(frames).map { Frame(null, null) })

class Frame(
    val roll1: PinCount?,
    val roll2: PinCount?,
) {
    val sumOfRolls: Score = when {
        roll1 != null && roll2 != null -> roll1 + roll2
        roll1 != null -> Score(roll1.value)
        else -> Score(0)
    }

    fun roll(pinCount: PinCount): Frame = when {
        roll1 == null -> Frame(pinCount, null)
        roll2 == null -> Frame(roll1, pinCount)
        else -> error("Tried to play a played frame")
    }
}

class Game(
    val score: Score = Score(0),
    val frames: List<Frame>
) {
    val scorecard: Scorecard
        get() = run {
            var score = Score(0)
            frames.map { frame ->
                val sumOfRolls = frame.sumOfRolls
                score = score + sumOfRolls
                val scoreString = when {
                    frame.roll1 == null -> ""
                    else -> score.rendered
                }
                "[${frame.roll1.rendered}][${frame.roll2.rendered}]$scoreString"
            }
        }

    fun roll(pinCount: PinCount): Game {
        val currentFrame = frames.find { it.roll1 == null || it.roll2 == null } ?: TODO()
        val newFrame = currentFrame.roll(pinCount)
        return Game(
            score + pinCount,
            frames.map { if (it == currentFrame) newFrame else it}
        )
    }
}

val PinCount?.rendered
    get() = when {
        this == null -> " "
        else -> this.value.toString()
    }

val Score.rendered
    get() = when (this.value) {
        0 -> ""
        else -> " " + this.value.toString()
    }
