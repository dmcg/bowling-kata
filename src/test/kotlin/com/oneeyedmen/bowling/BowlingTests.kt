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
        (game as IncompleteGame).hasState("Fred", 0, 0)

        game = game.roll(PinCount(4))
        (game as IncompleteGame).hasState("Fred", 4, 0)

        game = game.roll(PinCount(5))
        (game as IncompleteGame).hasState("Barney", 9, 0)

        game = game.roll(PinCount(1))
        (game as IncompleteGame).hasState("Barney", 9, 1)

        game = game.roll(PinCount(2))
        (game as CompleteGame).hasScores(9, 3)
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

