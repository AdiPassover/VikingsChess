package Exe.Comps;

import Exe.Pieces.ConcretePiece;
import Exe.ConcretePlayer;

import java.util.Comparator;

/**
 * The comparator used for ex3.
 */
public class DistanceComp implements Comparator<ConcretePiece> {
    ConcretePlayer _winner;
    public DistanceComp(ConcretePlayer winner) {
        this._winner = winner;
    }

    @Override
    public int compare(ConcretePiece p1, ConcretePiece p2) {
        if (p1.getDistance() > p2.getDistance())
            return -1;
        else if (p1.getDistance() < p2.getDistance())
            return 1;

        if (p1.getSerial() > p2.getSerial())
            return 1;
        else if (p1.getSerial() < p2.getSerial())
            return -1;

        if (p1.getOwner() == _winner && p2.getOwner() != _winner)
            return -1;
        return 1;
    }
}