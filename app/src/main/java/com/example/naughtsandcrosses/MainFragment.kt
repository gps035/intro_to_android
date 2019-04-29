package com.example.naughtsandcrosses

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseMvRxFragment() {
    private val viewModel: AppViewModel by fragmentViewModel()
    private lateinit var naughts: Drawable
    private lateinit var crosses: Drawable
    private lateinit var nothing: Drawable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        naughts = ContextCompat.getDrawable(context!!, R.drawable.ic_circle)!!
        crosses = ContextCompat.getDrawable(context!!, R.drawable.ic_cross)!!
        nothing = ContextCompat.getDrawable(context!!, R.color.white)!!

        // Call the tileClicked method when a user taps a tile
        row1Col1.setOnClickListener { viewModel.tileClicked(1, 1) }
        row1Col2.setOnClickListener { viewModel.tileClicked(1, 2) }
        row1Col3.setOnClickListener { viewModel.tileClicked(1, 3) }
        row2Col1.setOnClickListener { viewModel.tileClicked(2, 1) }
        row2Col2.setOnClickListener { viewModel.tileClicked(2, 2) }
        row2Col3.setOnClickListener { viewModel.tileClicked(2, 3) }
        row3Col1.setOnClickListener { viewModel.tileClicked(3, 1) }
        row3Col2.setOnClickListener { viewModel.tileClicked(3, 2) }
        row3Col3.setOnClickListener { viewModel.tileClicked(3, 3) }

        restart.setOnClickListener { viewModel.startNewGame() }
    }


    // This method updates the UI.
    // This should be the only place that we do this, and it should update based on the AppPresenter
    override fun invalidate() = withState(viewModel) { state ->
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
        restart.isInvisible = state.currentlyPlaying
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
        val imageToUse = when (state.boardState.getPlayerAtPosition(row, col)) {
            Player.Naughts -> naughts
            Player.Crosses -> crosses
            null -> nothing
        }

        tile.setImageDrawable(imageToUse)
    }
}