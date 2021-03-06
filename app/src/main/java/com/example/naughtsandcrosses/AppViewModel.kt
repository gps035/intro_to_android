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

    fun tileClicked(row: Int, col: Int) = withState {
        // If we aren't playing right now, do nothing
        if (!it.currentlyPlaying) {
            return@withState
        }
        // If a player has already claimed this tile, do nothing
        if (it.boardState.getPlayerAtPosition(row, col) != null) {
            return@withState
        }
        // Otherwise, the current player can claim this tile
        claimTile(row, col)
        // Now that we have updated the board we can query it's current state
        checkWinCondition()
        setNextPlayer()
    }

    private fun claimTile(row: Int, col: Int) = setState {
        copy(boardState = boardState.setPlayerAtPosition(row, col, currentPlayer))
    }

    private fun checkWinCondition() = setState {
        when {
            // If the current player has won, we can stop playing, and set the current player as the last winner
            boardState.playerHasWon(currentPlayer) -> copy(
                currentlyPlaying = false,
                lastWinner = currentPlayer
            )
            // Otherwise, if there are no tiles left to claim, it is a draw
            boardState.noFreeTiles() -> copy(
                currentlyPlaying = false,
                lastWinner = null
            )
            else -> this
        }
    }

    private fun setNextPlayer() = setState {
        copy(currentPlayer = nextPlayer(currentPlayer))
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
