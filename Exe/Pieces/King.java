package Exe.Pieces;

import Exe.Player;
import Exe.Position;

import java.util.LinkedList;

public class King extends ConcretePiece {

    public King(Player owner, Position start) {
        this._owner = owner;
        this._serial = 7;
        this._distance = 0;
        this._moves = new LinkedList<Position>();
        _moves.add(start);
    }

    public King(King k) {
        this._owner = k._owner;
        this._serial = 7;
        this._distance = k._distance;
        this._moves = new LinkedList<>(k._moves);
    }

    public boolean isKing() { return true; }

    /**
     * Get a Unicode character representing the type of the game piece.
     * <a href="https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode">...</a>
     *
     * @return A Unicode character representing the type of this game piece
     * (e.g., ♟ for pawn, ♞ for knight, ♜ for rook, etc.).
     */
    @Override
    public String getType() {
        return "♔";
    }

}
