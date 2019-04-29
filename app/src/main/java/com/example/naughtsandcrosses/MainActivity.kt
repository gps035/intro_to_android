package com.example.naughtsandcrosses

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val presenter = AppPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call the tileClicked method when a user taps a tile
        row1Col1.setOnClickListener { tileClicked(1, 1) }
        row1Col2.setOnClickListener { tileClicked(1, 2) }
        row1Col3.setOnClickListener { tileClicked(1, 3) }
        row2Col1.setOnClickListener { tileClicked(2, 1) }
        row2Col2.setOnClickListener { tileClicked(2, 2) }
        row2Col3.setOnClickListener { tileClicked(2, 3) }
        row3Col1.setOnClickListener { tileClicked(3, 1) }
        row3Col2.setOnClickListener { tileClicked(3, 2) }
        row3Col3.setOnClickListener { tileClicked(3, 3) }

        restart.setOnClickListener { startNewGame() }

        invalidate(presenter.state)
    }

    private fun startNewGame() {
        presenter.startNewGame().also { invalidate(it) }
    }

    private fun tileClicked(row: Int, col: Int) {
        presenter.tileClicked(row, col).also { invalidate(it) }
    }

    // This method updates the UI.
    // This should be the only place that we do this, and it should update based on the AppPresenter
    private fun invalidate(state: AppState) {
        setMessage(state)

        showOrHideStartButton(state)

        // Update each tile's image
        setTileImage(state, row1Col1, 1, 1)
        setTileImage(state, row1Col2, 1, 2)
        setTileImage(state, row1Col3, 1, 3)
        setTileImage(state, row2Col1, 2, 1)
        setTileImage(state, row2Col2, 2, 2)
        setTileImage(state, row2Col3, 2, 3)
        setTileImage(state, row3Col1, 3, 1)
        setTileImage(state, row3Col2, 3, 2)
        setTileImage(state, row3Col3, 3, 3)
    }

    private fun showOrHideStartButton(state: AppState) {
        restart.isVisible = !state.currentlyPlaying
    }

    private fun setMessage(state: AppState) {
        message.text = if (state.currentlyPlaying) {
            getCurrentPlayerMessage(state)
        } else {
            getWinnerText(state)
        }
    }

    private fun getCurrentPlayerMessage(state: AppState) = if (state.currentPlayer == Player.Naughts) {
        getString(R.string.naught_turn)
    } else {
        getString(R.string.cross_turn)
    }

    private fun getWinnerText(state: AppState) = when (state.lastWinner) {
        Player.Naughts -> getString(R.string.naught_win)
        Player.Crosses -> getString(R.string.cross_win)
        null -> getString(R.string.draw)
    }

    private fun setTileImage(state: AppState, tile: ImageView, row: Int, col: Int) {
        val imageToUse = getPlayerImageForTile(state, row, col)

        tile.setImageDrawable(imageToUse)
    }

    private fun getPlayerImageForTile(state: AppState, row: Int, col: Int) =
        when (state.boardState.getPlayerAtPosition(row, col)) {
            Player.Naughts -> getDrawable(R.drawable.ic_circle)
            Player.Crosses -> getDrawable(R.drawable.ic_cross)
            null -> getDrawable(R.color.white)
        }
}
