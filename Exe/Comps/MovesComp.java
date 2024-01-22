package Exe.Comps;

import Exe.Pieces.ConcretePiece;
import Exe.ConcretePlayer;

import java.util.Comparator;

/**
 * The comparator used for ex1.
 */
public class MovesComp implements Comparator<ConcretePiece> {
    ConcretePlayer _winner;
    public MovesComp(ConcretePlayer winner) {
        this._winner = winner;
    }
    @Override
    public int compare(ConcretePiece p1, ConcretePiece p2) {
        if (p1.getOwner() == _winner && p2.getOwner() != _winner)
            return -1;
        else if (p2.getOwner() == _winner && p1.getOwner() != _winner)
            return 1;

        if (p1.getNumberOfMoves() > p2.getNumberOfMoves())
            return 1;
        else if (p1.getNumberOfMoves() < p2.getNumberOfMoves())
            return -1;

        return p1.getSerial() - p2.getSerial();
    }
}