package com.oneeyedmen.bowling

sealed class Line(
    val player: Player,
    val frames: List<Frame>
) {

    fun scores(): List<Pair<Score, Frame>> = frames.windowed(size = 3, step = 1, partialWindows = true)
        .fold(emptyList()) { acc: List<Pair<Score, Frame>>, window: List<Frame> ->
            val thisFrame = window.first()
            val nextFrame = window.getOrNull(1)
            val nextNextFrame = window.getOrNull(2)
            val nextRoll = nextFrame?.roll1
            val nextNextRoll = nextFrame?.roll2 ?: if (nextFrame is Strike) nextNextFrame?.roll1 else null
            val thisScore = thisFrame.score(nextRoll, nextNextRoll)
            val accScore = (acc.lastOrNull()?.first ?: Score(0)) + thisScore
            acc + (accScore to thisFrame)
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