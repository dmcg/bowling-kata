package com.oneeyedmen.bowling

sealed class Line(
    val player: Player,
    val frames: List<Frame>
) {
    val score: Score = frames.windowed(size = 2, step = 1, partialWindows = true)
        .fold(Score(0)) { acc, window: List<Frame> ->
            val thisFrame: Frame = window.first()
            val nextFrame = if (window.size == 1) null else window[1]
            val (nextRoll, nextNextRoll) = nextFrame?.let { it.roll1 to it.roll2 } ?: (null to null)
            acc + thisFrame.score(nextRoll, nextNextRoll)
        }
}

class CompleteLine(player: Player, frames: List<Frame>) : Line(player, frames)

class IncompleteLine(
    player: Player,
    frames: List<Frame>
) : Line(player, frames) {

    constructor(
        player: Player,
        frames: Int = 10
    ) : this(player, (0.until(frames)).map { UnplayedFrame() })

    val currentFrame: IncompleteFrame =
        (frames.find { it is InProgressFrame } ?: frames.find { it is UnplayedFrame }
        ?: error("No playable frame")) as IncompleteFrame

    fun roll(pinCount: PinCount): Line {
        val newFrames = frames.replacing(currentFrame, currentFrame.roll(pinCount))
        return when {
            newFrames.last() is IncompleteFrame -> IncompleteLine(player, newFrames)
            else -> CompleteLine(player, newFrames)
        }
    }
}