package com.oneeyedmen.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTests {

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
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][ ]", "[ ][ ]")
        )

        game = game.roll(PinCount(4))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[4][ ] 4", "[ ][ ]")
        )

        game = game.roll(PinCount(6))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[4][/] 10", "[ ][ ]")
        )

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[4][/] 15", "[5][ ] 20")
        )

        game = game.roll(PinCount(1))
        (game as CompleteGame).hasScoreCard(
            listOf("Fred", "[4][/] 15", "[5][1] 21")
        )
    }

    @Test
    fun `spare as 10`() {
        var game: Game = newGame(listOf("Fred"), 2)
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][ ]", "[ ][ ]")
        )

        game = game.roll(PinCount(0))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[0][ ] 0", "[ ][ ]")
        )

        game = game.roll(PinCount(10))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[0][/] 10", "[ ][ ]")
        )

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[0][/] 15", "[5][ ] 20")
        )

        game = game.roll(PinCount(1))
        (game as CompleteGame).hasScoreCard(
            listOf("Fred", "[0][/] 15", "[5][1] 21")
        )
    }

    @Test
    fun strike() {
        var game: Game = newGame(listOf("Fred"), 2)
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][ ]", "[ ][ ]")
        )

        game = game.roll(PinCount(10))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][X] 10", "[ ][ ]")
        )

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][X] 15", "[5][ ] 20")
        )

        game = game.roll(PinCount(1))
        (game as CompleteGame).hasScoreCard(
            listOf("Fred", "[ ][X] 16", "[5][1] 22")
        )
    }

    @Test
    fun `two strikes in a row`() {
        var game: Game = newGame(listOf("Fred"), 3)
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][ ]", "[ ][ ]", "[ ][ ]")
        )

        game = game.roll(PinCount(10))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][X] 10", "[ ][ ]", "[ ][ ]")
        )

        game = game.roll(PinCount(10))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][X] 20", "[ ][X] 30", "[ ][ ]")
        )

        game = game.roll(PinCount(1))
        (game as IncompleteGame).hasScoreCard(
            listOf("*Fred", "[ ][X] 21", "[ ][X] 32", "[1][ ] 33")
        )

        game = game.roll(PinCount(2))
        (game as CompleteGame).hasScoreCard(
            listOf("Fred", "[ ][X] 21", "[ ][X] 34", "[1][2] 37")
        )
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

private fun Line.toFrameStrings(): List<String> = listOf(player) +
        this.scores().map { (score, frame) -> frame.toFrameString(score) }

private fun Frame.toFrameString(accScore: Score): String {
    val scoreString = accScore.value.toString()
    return when (this) {
        is UnplayedFrame -> "[ ][ ]"
        is InProgressFrame -> "[${this.roll1.value}][ ] $scoreString"
        is PlainCompleteFrame -> "[${this.roll1.value}][${this.roll2.value}] $scoreString"
        is Spare -> "[${this.roll1.value}][/] $scoreString"
        is Strike -> "[ ][X] $scoreString"
        else -> "??"
    }
}


