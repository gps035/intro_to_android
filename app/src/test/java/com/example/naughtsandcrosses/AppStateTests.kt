package com.example.naughtsandcrosses

import com.airbnb.mvrx.test.MvRxTestRule
import com.airbnb.mvrx.withState
import org.junit.Test

import org.junit.Assert.*
import org.junit.ClassRule

class AppStateTests {
    @Test
    fun canWinDiagonally() {
        val viewModel = AppViewModel(AppState())
            .apply { tileClicked(1, 1) }
            .apply { tileClicked(1, 2) }
            .apply { tileClicked(1, 3) }
            .apply { tileClicked(2, 1) }
            .apply { tileClicked(2, 2) }
            .apply { tileClicked(2, 3) }
            .apply { tileClicked(3, 1) }
        withState(viewModel) {
            assertEquals(false, it.currentlyPlaying)
            assertEquals(Player.Naughts, it.lastWinner)
        }
    }

    @Test
    fun canDraw() {
        val viewModel = AppViewModel(AppState())
            .apply { tileClicked(1, 1) }
            .apply { tileClicked(1, 2) }
            .apply { tileClicked(1, 3) }
            .apply { tileClicked(2, 2) }
            .apply { tileClicked(2, 1) }
            .apply { tileClicked(2, 3) }
            .apply { tileClicked(3, 2) }
            .apply { tileClicked(3, 1) }
            .apply { tileClicked(3, 3) }
        withState(viewModel) {
            assertEquals(false, it.currentlyPlaying)
            assertEquals(null, it.lastWinner)
        }
    }

    companion object {
        @JvmField
        @ClassRule
        val mvrxTestRule = MvRxTestRule()
    }
}
