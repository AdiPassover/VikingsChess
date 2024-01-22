package Exe;

public class Position {

    private final int x;
    private final int y;



    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
    /**
     * @return true if the position is a corner, false otherwise.
     */
    public boolean isCorner(){
        return (x == 0 && y == 0) || (x == 0 && y == 10) || (x == 10 && y == 0) || (x == 10 && y == 10);
    }

}
