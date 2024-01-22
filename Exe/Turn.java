package Exe;

import Exe.Pieces.ConcretePiece;
import Exe.Pieces.Pawn;

import java.util.LinkedList;

/**
 * The Turn class is responsible to keeping data of each turn in order to restore
 * the game to its previous state after the user presses back.
 */
public class Turn {

    private final Position[] _movement = new Position[2]; // [from,to]
    private final ConcretePiece _pieceMoved; // The piece that was moved this turn
    /**
     * Saves the pieces that were captured this turn.
     * A piece that was captured in the i-th direction of the current
     * piece will be placed in the i-th cell in this array
     * using the order of directions saved in GameLogic.DIRECTIONS.
     */
    private final Pawn[] _piecesCaptured;

    public Turn(Position startPos, Position endPos, ConcretePiece pieceMoved, Pawn[] piecesCaptured) {
        _movement[0] = startPos;
        _movement[1] = endPos;
        this._pieceMoved = pieceMoved;
        this._piecesCaptured = piecesCaptured;
    }

    /**
     * Restores the game to his state before this turn.
     * This function handles everything except for switching from
     * switching turns between the players.
     *
     * @param board The board after the turn
     * @param kingPos The position of the king after the turn
     * @param piecesPlaced The piece counter after the turn
     */
    public void restore(ConcretePiece[][] board, int[] kingPos, int[][][] piecesPlaced, LinkedList<ConcretePiece> captured) {
        // restore the movement
        board[_movement[1].y()][_movement[1].x()] = null;
        board[_movement[0].y()][_movement[0].x()] = _pieceMoved;

        // restore the kills
        for (int i = 0; i < 4; i++) {
            if (_piecesCaptured[i] != null) {
                int[] d = GameLogic.DIRECTIONS[i];
                board[_movement[1].y()+d[1]][_movement[1].x()+d[0]] = _piecesCaptured[i];
                for (int j = 0; j < captured.size(); j++) {
                    if (captured.get(j).getSerial() == _piecesCaptured[i].getSerial()) {
                        captured.remove(j);
                        j--;
                    }
                }
            }
        }

        // restore piecePlaced
        if (_pieceMoved.getOwner().isPlayerOne())
            piecesPlaced[_movement[1].y()][_movement[1].x()][_pieceMoved.getSerial()-1]--;
        else
            piecesPlaced[_movement[1].y()][_movement[1].x()][_pieceMoved.getSerial()+12]--;

        // restore kingPos
        if (_pieceMoved.isKing()) {
            kingPos[0] = _movement[0].x();
            kingPos[1] = _movement[0].y();
        }
    }

}
