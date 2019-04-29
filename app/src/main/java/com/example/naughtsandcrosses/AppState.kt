package com.example.naughtsandcrosses

import com.airbnb.mvrx.MvRxState

data class AppState(
    val boardState: BoardState = BoardState(),
    val lastWinner: Player? = null,
    val currentPlayer: Player = Player.Naughts,
    val currentlyPlaying: Boolean = true
) : MvRxState