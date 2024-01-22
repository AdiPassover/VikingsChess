package Exe.Pieces;

import Exe.Player;
import Exe.Position;

import java.util.LinkedList;

public class Pawn extends ConcretePiece {
    private int _killCount;


    public Pawn(Player owner, int serial, Position start) {
        this._owner = owner;
        this._killCount = 0;
        this._serial = serial;
        this._distance = 0;
        this._moves = new LinkedList<Position>();
        _moves.add(start);
    }
    public void incrementKills() {
        _killCount++;
    }
    public Pawn(Pawn p) {
        this._owner = p._owner;
        this._killCount = p._killCount;
        this._serial = p._serial;
        this._distance = p._distance;
        this._moves = new LinkedList<>(p._moves);
    }
    public int getKills(){
        return _killCount;
    }

    public boolean isKing() { return false; }

    /**
     * Get a Unicode character representing the type of the game piece.
     * <a href="https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode">...</a>
     *
     * @return A Unicode character representing the type of this game piece
     * (e.g., ♟ for pawn, ♞ for knight, ♜ for rook, etc.).
     */
@Override
    public String getType() {
        if (getOwner().isPlayerOne())
            return "♙";
        return "♟︎";
    }
}
