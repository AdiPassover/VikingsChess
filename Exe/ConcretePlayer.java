package Exe;

public class ConcretePlayer implements Player {

    private int _wins;
    private final boolean _PlayerOne;

    public ConcretePlayer(boolean isPlayerOne){
          _PlayerOne = isPlayerOne;
          _wins = 0;
    }

    /**
     * @return true if the player is player 1, false otherwise.
     */
    @Override
    public boolean isPlayerOne() {
        return _PlayerOne;
    }

    /**
     * Get the number of wins achieved by the player in the game.
     *
     * @return The total number of wins by the player.
     */
    @Override
    public int getWins() {
        return _wins;
    }

    /**
     * Adds a win to the player.
     */
    public void addWin() { _wins++; }
}
