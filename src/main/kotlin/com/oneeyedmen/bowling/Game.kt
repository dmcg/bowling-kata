package com.oneeyedmen.bowling

typealias Player = String

sealed class Game(
    val lines: List<Line>
)

class CompleteGame(lines: List<Line>) : Game(lines)

class IncompleteGame(lines: List<Line>) : Game(lines) {
    fun roll(pinCount: PinCount): Game {
        val newLines = lines.replacing(currentLine, currentLine.roll(pinCount))
        return when {
            newLines.any { it is PlayableLine } -> IncompleteGame(newLines)
            else -> CompleteGame(newLines)
        }
    }

    private val currentLine: PlayableLine =
        (lines.filterIsInstance<PlayableLine>().find { it.currentFrame is IncompleteFrame }
            ?: lines.maxBy { it.frames.count { it is UnplayedFrame } }) as PlayableLine

    val toRoll: Player = currentLine.player
}

fun newGame(players: List<Player>, frames: Int = 10) = IncompleteGame(players.map { PlayableLine(it, frames) })
