package Exe.Pieces;

import Exe.Player;
import Exe.Position;

import java.util.LinkedList;

public abstract class ConcretePiece implements Piece {

    protected Player _owner;
    protected int _serial; // The serial number of the piece TODO: maybe this should be final
    protected int _distance; // Distance the piece traveled
    protected LinkedList<Position> _moves; // List of tiles the piece have traveled in order


    public Player getOwner() {
        return _owner;
    }
    public int getSerial(){
        return _serial;
    }

    public int getDistance() {
        return _distance;
    }
    public void setDistance(int dis){
        if (dis>=0) _distance = dis;
    }

    public void addMove(Position pos){
        _moves.add(pos);
    }
    public int getNumberOfMoves(){
        return _moves.size();
    }
    public Position getMoves(int index){
        return _moves.get(index);
    }
    public abstract boolean isKing();

    public abstract String getType();

}
