package Exe.Comps;

import Exe.Position;

import java.util.Comparator;

/**
 * The comparator used for ex4.
 */
public class PiecesComp implements Comparator<Position> {

    int[][][] _piecePlaced;

    public PiecesComp(int[][][] piecesPlaced) {
        this._piecePlaced = piecesPlaced;
    }

    @Override
    public int compare(Position p1, Position p2) {
        // The number of different pieces that traveled on this tile is the numbers of non-zero cells
        int c1 = 0;
        for (int i = 0; i < _piecePlaced[p1.y()][p1.x()].length; i++) {
            if (_piecePlaced[p1.y()][p1.x()][i] != 0) {
                c1++;
            }
        }
        int c2 = 0;
        for (int i = 0; i < _piecePlaced[p2.y()][p2.x()].length; i++) {
            if (_piecePlaced[p2.y()][p2.x()][i] != 0) {
                c2++;
            }
        }

        if (c1 > c2)
            return -1;
        if (c1 < c2)
            return 1;

        if (p1.x() > p2.x())
            return 1;
        if (p1.x() < p2.x())
            return -1;

        if (p1.y() > p2.y())
            return 1;
        return -1;
    }

}


