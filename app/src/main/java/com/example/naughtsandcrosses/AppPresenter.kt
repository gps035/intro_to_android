package com.example.naughtsandcrosses

import com.airbnb.mvrx.BaseMvRxViewModel

class AppViewModel(initialState: AppState) : BaseMvRxViewModel<AppState>(initialState, debugMode = BuildConfig.DEBUG) {

    fun startNewGame() = setState {
        copy(
            currentPlayer = startingPlayer(this),
            currentlyPlaying = true,
            boardState = this.boardState.clearBoard()
        )
    }

    fun tileClicked(row: Int, col: Int) = setState {
        // If we aren't playing right now, do nothing
        if (!currentlyPlaying) {
            return@setState this
        }
        // If a player has already claimed this tile, do nothing
        if (boardState.getPlayerAtPosition(row, col) != null) {
            return@setState this
        }
        val newBoardState = boardState.setPlayerAtPosition(row, col, currentPlayer)
        // Otherwise, the current player can claim this tile
        var nowCurrentlyPlaying = currentlyPlaying
        var newLastWinner = lastWinner
        // Now that we have updated the board we can query it's current state
        if (newBoardState.playerHasWon(currentPlayer)) {
            // If the current player has won, we can stop playing, and set the current player as the last winner
            nowCurrentlyPlaying = false
            newLastWinner = currentPlayer
        } else if (newBoardState.noFreeTiles()) {
            // Otherwise, if there are no tiles left to claim, it is a draw
            nowCurrentlyPlaying = false
            newLastWinner = null
        }
        return@setState copy(
            boardState = newBoardState,
            currentPlayer = nextPlayer(currentPlayer),
            currentlyPlaying = nowCurrentlyPlaying,
            lastWinner = newLastWinner
        )
    }

    // Here we swap the current player
    private fun nextPlayer(player: Player) = if (player == Player.Crosses) {
        Player.Naughts
    } else {
        Player.Crosses
    }

    // Here we choose a starting player based on the last winner, the previous winner goes second
    private fun startingPlayer(state: AppState) = state.lastWinner.let {
        if (it == null) {
            nextPlayer(state.currentPlayer)
        } else {
            nextPlayer(it)
        }
    }
}
