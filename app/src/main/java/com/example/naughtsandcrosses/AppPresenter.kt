package com.example.naughtsandcrosses

data class AppState(
    val boardState: BoardState = BoardState(),
    val lastWinner: Player? = null,
    val currentPlayer: Player = Player.Naughts,
    val currentlyPlaying: Boolean = true
)

class AppPresenter {
    var state = AppState()
    private set

    fun startNewGame() = state.copy(
        currentPlayer = startingPlayer(state),
        currentlyPlaying = true,
        boardState = state.boardState.clearBoard()
    ).also { state = it }

    fun tileClicked(row: Int, col: Int): AppState {
        // If we aren't playing right now, do nothing
        if (!state.currentlyPlaying) {
            return state
        }
        // If a player has already claimed this tile, do nothing
        if (state.boardState.getPlayerAtPosition(row, col) != null) {
            return state
        }
        // Otherwise, the current player can claim this tile
        state = state.copy(boardState = state.boardState.setPlayerAtPosition(row, col, state.currentPlayer))

        // Now that we have updated the board we can query it's current state
        if (state.boardState.playerHasWon(state.currentPlayer)) {
            // If the current player has won, we can stop playing, and set the curent player as the last winner
            state = state.copy(lastWinner = state.currentPlayer, currentlyPlaying = false)
        } else if (state.boardState.noFreeTiles()) {
            // Otherwise, if there are no tiles left to claim, it is a draw
            state = state.copy(lastWinner = null, currentlyPlaying = false)
        }
        return state.copy(currentPlayer = nextPlayer(state.currentPlayer)).also { state = it }
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
