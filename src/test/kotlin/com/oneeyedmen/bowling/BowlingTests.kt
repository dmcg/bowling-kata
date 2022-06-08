package com.oneeyedmen.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTests {

    @Test
    fun `new game`() {
        val game: IncompleteGame = newGame(listOf("Fred", "Barney"))
        game.hasState("Fred", 0, 0)
    }

    @Test
    fun `play a short game`() {
        var game: Game = newGame(listOf("Fred", "Barney"), 1)
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][ ]"),
            listOf("Barney", "[ ][ ]")
        )

        game = game.roll(PinCount(4))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[4][ ] 4"),
            listOf("Barney", "[ ][ ]")
        )

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasScoreCard(
            listOf("Fred", "[4][5] 9"),
            listOf("*Barney", "[ ][ ]")
        )

        game = game.roll(PinCount(1))
        (game as IncompleteGame).hasScoreCard(
            listOf("Fred", "[4][5] 9"),
            listOf("*Barney", "[1][ ] 1")
        )

        game = game.roll(PinCount(2))
        (game as CompleteGame).hasScoreCard(
            listOf("Fred", "[4][5] 9"),
            listOf("Barney", "[1][2] 3")
        )
    }

    @Test
    fun spare() {
        var game: Game = newGame(listOf("Fred"), 2)
        (game as IncompleteGame).hasState("Fred", 0)

        game = game.roll(PinCount(4))
        (game as IncompleteGame).hasState("Fred", 4)

        game = game.roll(PinCount(6))
        (game as IncompleteGame).hasState("Fred", 10)

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasState("Fred", 20)

        game = game.roll(PinCount(1))
        (game as CompleteGame).hasScores(21)
    }

    @Test
    fun `spare as 10`() {
        var game: Game = newGame(listOf("Fred"), 2)
        (game as IncompleteGame).hasState("Fred", 0)

        game = game.roll(PinCount(0))
        (game as IncompleteGame).hasState("Fred", 0)

        game = game.roll(PinCount(10))
        (game as IncompleteGame).hasState("Fred", 10)

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasState("Fred", 20)

        game = game.roll(PinCount(1))
        (game as CompleteGame).hasScores(21)
    }

    @Test
    fun strike() {
        var game: Game = newGame(listOf("Fred"), 2)
        (game as IncompleteGame).hasState("Fred", 0)

        game = game.roll(PinCount(10))
        (game as IncompleteGame).hasState("Fred", 10)

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasState("Fred", 20)

        game = game.roll(PinCount(1))
        (game as CompleteGame).hasScores(22)
    }

    @Test
    fun `two strikes in a row`() {
    }
}

private fun Game.hasScoreCard(vararg lineStrings: List<String>) {
    assertEquals(lineStrings.asList(), toScoreCard())
}

private fun Game.toScoreCard(): List<List<String>> {
    val lines = lines.map(Line::toFrameStrings)
    return when (this) {
        is CompleteGame -> lines
        is IncompleteGame -> {
            val player = this.toRoll
            lines.map { if (it.first() == player) it.replacing(player, "*$player") else it }
        }
    }
}

private fun Line.toFrameStrings(): List<String> = listOf(player) + frames.map { it.toFrameString() }

private fun Frame.toFrameString(): String = when (this) {
    is UnplayedFrame -> "[ ][ ]"
    is InProgressFrame -> "[${this.roll1.value}][ ] ${this.score(null, null).value}"
    is PlainCompleteFrame -> "[${this.roll1.value}][${this.roll2.value}] ${this.score(null, null).value}"
    else -> "??"
}

private fun IncompleteGame.hasState(
    toPlay: String,
    vararg scores: Int,
) {
    assertEquals(toPlay, toRoll)
    hasScores(*scores)
}

private fun Game.hasScores(vararg scores: Int) {
    val actualScores = lines.map(Line::score)
    val expectedScores = scores.map(::Score)
    assertEquals(expectedScores, actualScores)
}



