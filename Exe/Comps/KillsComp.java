package Exe.Comps;
import Exe.ConcretePlayer;
import Exe.Pieces.Pawn;
import java.util.Comparator;

/**
 * The comparator used for ex2.
 */
public class KillsComp implements Comparator<Pawn> {
    ConcretePlayer _winner;
    public KillsComp(ConcretePlayer winner) {
        this._winner = winner;
    }
    @Override
    public int compare(Pawn p1, Pawn p2) {
        if (p1.getKills() > p2.getKills())
            return -1;
        else if (p1.getKills() < p2.getKills())
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