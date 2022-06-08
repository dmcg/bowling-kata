package com.oneeyedmen.bowling

sealed class Frame {
    open val roll1: PinCount? = null
    open val roll2: PinCount? = null
    abstract fun score(nextRoll: PinCount?, nextNextRoll: PinCount?): Score
}

sealed class IncompleteFrame : Frame() {
    abstract fun roll(pinCount: PinCount): Frame
}

class UnplayedFrame : IncompleteFrame() {
    override fun score(nextRoll: PinCount?, nextNextRoll: PinCount?) = Score(0)
    override fun roll(pinCount: PinCount): Frame =
        when (pinCount.value) {
            10 -> Strike()
            else -> InProgressFrame(pinCount)
        }
}

class InProgressFrame(
    override val roll1: PinCount
) : IncompleteFrame() {
    override fun score(nextRoll: PinCount?, nextNextRoll: PinCount?) = roll1.asScore()
    override fun roll(pinCount: PinCount) = when ((roll1 + pinCount).value) {
        10 -> Spare(roll1, pinCount)
        else -> PlainCompleteFrame(roll1, pinCount)
    }
}

sealed class CompleteFrame(
    override val roll1: PinCount,
) : Frame()

class PlainCompleteFrame(
    roll1: PinCount,
    override val roll2: PinCount,
) : CompleteFrame(roll1) {
    override fun score(nextRoll: PinCount?, nextNextRoll: PinCount?) = roll1 + roll2
}

class Spare(
    roll1: PinCount,
    override val roll2: PinCount
) : CompleteFrame(roll1) {
    init {
        require((roll1 + roll2).value == 10)
    }

    override fun score(nextRoll: PinCount?, nextNextRoll: PinCount?) =
        when {
            nextRoll == null -> roll1 + roll2
            else -> roll1 + roll2 + nextRoll
        }
}

class Strike : CompleteFrame(PinCount(10)) {
    override fun score(nextRoll: PinCount?, nextNextRoll: PinCount?) =
        when {
            nextRoll == null -> Score(10)
            nextNextRoll == null -> Score(10) + nextRoll
            else -> Score(10) + nextRoll + nextNextRoll
        }
}