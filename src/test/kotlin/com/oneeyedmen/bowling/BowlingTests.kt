package com.oneeyedmen.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTests {

    @Test
    fun `new game`() {
        val game: IncompleteGame = newGame(listOf("Fred", "Barney"))
        assertEquals("Fred", game.toRoll)
        assertEquals(Score(0), game.lines[0].score)
        assertEquals(Score(0), game.lines[1].score)
    }

    @Test
    fun `play a short game`() {
        var game: Game = newGame(listOf("Fred", "Barney"), 1)
        game as IncompleteGame
        assertEquals("Fred", game.toRoll)
        assertEquals(Score(0), game.lines[0].score)
        assertEquals(Score(0), game.lines[1].score)

        game = game.roll(PinCount(4)) as IncompleteGame
        assertEquals("Fred", game.toRoll)
        assertEquals(Score(4), game.lines[0].score)
        assertEquals(Score(0), game.lines[1].score)

        game = game.roll(PinCount(5)) as IncompleteGame
        assertEquals("Barney", game.toRoll)
        assertEquals(Score(9), game.lines[0].score)
        assertEquals(Score(0), game.lines[1].score)

        game = game.roll(PinCount(1)) as IncompleteGame
        assertEquals("Barney", game.toRoll)
        assertEquals(Score(9), game.lines[0].score)
        assertEquals(Score(1), game.lines[1].score)

        game = game.roll(PinCount(2)) as CompleteGame
        assertEquals(Score(9), game.lines[0].score)
        assertEquals(Score(3), game.lines[1].score)
    }
}