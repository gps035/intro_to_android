package com.example.naughtsandcrosses

// We represent the board as a 2-dimensional list of Players
data class BoardState(
    private val boardState: List<List<Player?>> = listOf(
        listOf<Player?>(null, null, null),
        listOf<Player?>(null, null, null),
        listOf<Player?>(null, null, null)
    )
) {
    fun getPlayerAtPosition(row: Int, col: Int): Player? {
        // We subtract 1 on each, because array indexes start at 0, not 1
        return boardState[row - 1][col - 1]
    }

    fun setPlayerAtPosition(row: Int, col: Int, player: Player): BoardState {
        // We subtract 1 on each, because array indexes start at 0, not 1
        return this.copy(boardState = boardState.toMutableList().apply {
            this[row - 1] = this[row - 1].toMutableList().apply {
                this[col - 1] = player
            }
        })
    }

    fun playerHasWon(player: Player): Boolean {
        return winningHorizontally(player)
                || winningVertically(player)
                || winningDiagonally(player)
    }

    private fun winningHorizontally(player: Player): Boolean {
        for (num in 0..2) {
            if (boardState[num][0] == player &&
                boardState[num][1] == player &&
                boardState[num][2] == player
            ) {
                return true
            }
        }
        return false
    }

    private fun winningVertically(player: Player): Boolean {
        for (num in 0..2) {
            if (boardState[0][num] == player &&
                boardState[1][num] == player &&
                boardState[2][num] == player
            ) {
                return true
            }
        }
        return false
    }

    private fun winningDiagonally(player: Player): Boolean {
        if (boardState[0][0] == player &&
            boardState[1][1] == player &&
            boardState[2][2] == player
        ) {
            return true
        }
        if (boardState[0][2] == player &&
            boardState[1][1] == player &&
            boardState[2][0] == player
        ) {
            return true
        }
        return false
    }

    fun clearBoard(): BoardState {
        // Return a new board state
        return BoardState()
    }

    fun noFreeTiles() = !freePositions().any()

    fun freePositions(): List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until boardState.count()) {
            for (col in 0 until boardState[row].count()) {
                if (boardState[row][col] == null) {
                    positions.add(Pair(row + 1, col + 1))
                }
            }
        }
        return positions
    }
}