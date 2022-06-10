package com.oneeyedmen.bowling

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BowlingTests {

    @Test
    fun `scorecard for a single frame game`() {
        val game: Game = newGame(1)
        assertEquals(
            listOf("[ ][ ]"),
            game.scorecard
        )
    }

    @Test
    fun `first roll scorecard for a single frame game`() {
        var game: Game = newGame(1)
        game = game.roll(PinCount(0))
        assertEquals(
            listOf("[0][ ]"),
            game.scorecard
        )
    }

    @Test
    fun `single frame`() {
        var game: Game = newGame(1)

        game = game.roll(PinCount(3))
        assertEquals(
            listOf("[3][ ] 3"),
            game.scorecard
        )
        assertEquals(Score(3), game.score)
        game = game.roll(PinCount(4))
        assertEquals(
            listOf("[3][4] 7"),
            game.scorecard
        )
        assertEquals(Score(7), game.score)
    }

    @Test
    fun `two frames`() {
        var game: Game = newGame(2)
        assertEquals(
            listOf("[ ][ ]", "[ ][ ]"),
            game.scorecard
        )

        game = game.roll(PinCount(3))
        assertEquals(
            listOf("[3][ ] 3", "[ ][ ]"),
            game.scorecard
        )
        assertEquals(Score(3), game.score)
        game = game.roll(PinCount(4))
        assertEquals(
            listOf("[3][4] 7", "[ ][ ]"),
            game.scorecard
        )
        assertEquals(Score(7), game.score)

        game = game.roll(PinCount(2))
        assertEquals(
            listOf("[3][4] 7", "[2][ ] 9"),
            game.scorecard
        )
        assertEquals(Score(9), game.score)
        game = game.roll(PinCount(7))
        assertEquals(
            listOf("[3][4] 7", "[2][7] 16"),
            game.scorecard
        )
        assertEquals(Score(16), game.score)
    }

}

