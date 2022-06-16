package com.oneeyedmen.bowling

typealias Game = List<Line>

class Line(
    val player: Player,
    val frames: List<Frame>
) {
    init {
        require(frames.isNotEmpty())
    }
}

typealias Player = String

class Frame(
    val roll1: PinCount?,
    val roll2: PinCount?,
    val roll3: PinCount?,
) {
    init {
        if (roll3 == null) require(roll2 == null)
        if (roll2 == null) require(roll1 == null)
    }
}

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

enum class FrameType {
    Unplayed, InProgress, Strike, Spare, Complete
}