package com.example.naughtsandcrosses

import org.junit.Test

import org.junit.Assert.*

class AppStateTests {
    @Test
    fun canWinDiagonally() {
        val state = AppPresenter()
            .apply { tileClicked(1,1) }
            .apply { tileClicked(1,2) }
            .apply { tileClicked(1,3) }
            .apply { tileClicked(2,1) }
            .apply { tileClicked(2,2) }
            .apply { tileClicked(2,3) }
            .apply { tileClicked(3,1) }
            .let { it.state }
        assertEquals(false, state.currentlyPlaying)
        assertEquals(Player.Naughts, state.lastWinner)
    }

    @Test
    fun canDraw() {
        val state = AppPresenter()
            .apply { tileClicked(1,1) }
            .apply { tileClicked(1,2) }
            .apply { tileClicked(1,3) }
            .apply { tileClicked(2,2) }
            .apply { tileClicked(2,1) }
            .apply { tileClicked(2,3) }
            .apply { tileClicked(3,2) }
            .apply { tileClicked(3,1) }
            .apply { tileClicked(3,3) }
            .let { it.state }
        assertEquals(false, state.currentlyPlaying)
        assertEquals(null, state.lastWinner)
    }
}
