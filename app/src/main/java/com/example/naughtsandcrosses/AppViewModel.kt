package com.example.naughtsandcrosses

import com.airbnb.mvrx.BaseMvRxViewModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AppViewModel(initialState: AppState) : BaseMvRxViewModel<AppState>(initialState, debugMode = BuildConfig.DEBUG) {

    fun startNewGame() {
        resetBoard()
        withState {
            if (it.currentPlayer == Player.Crosses) {
                chooseTile()
            }
        }
    }

    private fun resetBoard() = setState {
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
        // Computer is playing, do nothing
        if (it.currentPlayer != Player.Naughts) {
            return@withState
        }
        // If a player has already claimed this tile, do nothing
        if (it.boardState.getPlayerAtPosition(row, col) != null) {
            return@withState
        }
        // Otherwise, the current player can claim this tile
        tileChosen(row, col)
        chooseTile()
    }

    private fun tileChosen(row: Int, col: Int) {
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
                currentlyPlaying = false
            )
            else -> this
        }
    }

    private fun setNextPlayer() = setState {
        copy(currentPlayer = nextPlayer(currentPlayer))
    }

    private fun chooseTile() = withState {
        val positions = it.boardState.freePositions()
        // Otherwise, the current player can claim this tile
        Flowable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .map { positions.shuffled()[0] }
            .subscribe { position -> tileChosen(position.first, position.second) }
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
