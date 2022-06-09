package com.oneeyedmen.bowling

fun newGame(players: List<Player>, frames: Int = 10) =
    IncompleteGame(players.map { IncompleteLine(it, frames) })

sealed class Game(
    val lines: List<Line>
)

class CompleteGame(lines: List<Line>) : Game(lines)

class IncompleteGame(lines: List<Line>) : Game(lines) {

    fun roll(pinCount: PinCount): Game {
        val newLines = lines.replacing(currentLine, currentLine.roll(pinCount))
        return when {
            newLines.any { it is IncompleteLine } -> IncompleteGame(newLines)
            else -> CompleteGame(newLines)
        }
    }

    private val currentLine: IncompleteLine = (
            lines
                .filterIsInstance<IncompleteLine>()
                .find { it.currentFrame is InProgressFrame }
                ?: lines.maxBy(Line::numberOfUnplayedFrames)
            ) as IncompleteLine

    val toRoll: Player = currentLine.player
}

private fun Line.numberOfUnplayedFrames() = frames.count { it is UnplayedFrame }

