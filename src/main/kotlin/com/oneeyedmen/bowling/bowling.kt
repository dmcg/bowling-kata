package com.oneeyedmen.bowling

@JvmInline
value class PinCount(
    val value: Int
) {
    init {
        require(value in 0..10)
    }
}

@JvmInline
value class Score(
    val value: Int
) {
    init {
        require(value in 0..300)
    }
}

typealias Player = String

sealed class Frame {
    abstract val roll1: PinCount?
}

class Strike : Frame() {
    override val roll1: PinCount = PinCount(10)
}

class Spare(
    override val roll1: PinCount,
    val roll2: PinCount
) : Frame()

class UnplayedFrame : Frame() {
    override val roll1: PinCount? = null
}

class CompletedFrame(
    override val roll1: PinCount,
    val roll2: PinCount
) : Frame()

class PartiallyCompletedFrame(
    override val roll1: PinCount
) : Frame()

class Line(
    val player: Player,
    val frames: List<Frame>
)

sealed class Game(
    val lines: List<Line>
)

class CompletedGame(
    lines: List<Line>
) : Game(lines)

class InProgressGame(
    lines: List<Line>
) : Game(lines)

fun InProgressGame.roll(pinCount: PinCount): Game = TODO()

fun newGame(): InProgressGame = TODO()
