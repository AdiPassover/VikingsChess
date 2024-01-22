package Exe;

import Exe.Comps.*;
import Exe.Pieces.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;


public class GameLogic implements PlayableLogic {

    private ConcretePiece[][] _board;
    private final ConcretePlayer _player1;
    private final ConcretePlayer _player2;
    private final int _boardSize = 11;
    /**
     * Counts how many times each piece was placed on each tile.
     * For each tile, the first 13 cells are for player one pieces,
     * and the next 24 are for player two.
     */
    private int[][][] _piecesPlaced = new int[_boardSize][_boardSize][37];
    private Stack<Turn> _turns = new Stack<Turn>();
    private boolean _isPlayerOneTurn = true;
    private int[] _kingPos = new int[2]; // The position of the king: [col,row].
    public static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // A 2D array of the 4 directions: up, down, left, right.
    private LinkedList<ConcretePiece> _capturedPieces = new LinkedList<>(); // A list of all the pieces that were captured.

    /**
     * Create a new game logic instance.
     * The game logic constructor should init the player, then calls for the reset function, which initializes the board,
     * and all the needed fields.
     */
    public GameLogic() {
        _player1 = new ConcretePlayer(true);
        _player2 = new ConcretePlayer(false);
        reset();
    }

    /**
     * Attempt to move a piece from one position to another on the game board.
     *
     * @param a The starting position of the piece.
     * @param b The destination position for the piece.
     * @return true if the move is valid and successful, false otherwise.
     */
    @Override
    public boolean move(Position a, Position b) {
        ConcretePiece piece = _board[a.y()][a.x()];
        if (!isMoveValid(a,b,piece)) return false;

         //handling the actual movement of the piece
        // Placing the piece in the destination
        if (piece.isKing()) {
            _board[b.y()][b.x()] = new King((King)piece);
            _kingPos[0] = b.x();
            _kingPos[1] = b.y();
        }
        else
            _board[b.y()][b.x()] = new Pawn((Pawn)piece);
        _board[a.y()][a.x()] = null; // Deleting the piece from starting position
        Pawn[] captured = capture(b);// Calls the capture function to check if the piece captured any other pieces. If so, it returns them in an array.
        _isPlayerOneTurn = !_isPlayerOneTurn; // Switching turns
        updateStats(a,b,piece,captured);// Update the statistics

        return true;
    }
    /**
     * Check if a move is valid.
     * A move will be valid if: the piece is not null, it's the correct player's turn,
     * the piece is moving in the same row or column,
     * the piece is moving to a different tile,
     * the piece is not moving to a corner (unless it's the King), the destination is empty,
     * the tiles the piece is moving through are empty.
     * @param a The starting position of the piece.
     * @param b The destination position for the piece.
     * @param piece The piece that is moving.
     * @return true if the move is valid, false otherwise.
     */

    private boolean isMoveValid(Position a, Position b, ConcretePiece piece) {

        if (piece == null)
            return false;
        if (piece.getOwner() == _player1 && !_isPlayerOneTurn) // Checking it's the correct player's turn
            return false;
        if (piece.getOwner() == _player2 && _isPlayerOneTurn)
            return false;
        if (a.x() != b.x() && a.y() != b.y()) // Checking if the piece is moving in the same row or column
            return false;
        if (a.x() == b.x() && a.y() == b.y()) // Checking the piece is moving to a different tile
            return false;
        if (!piece.isKing() && b.isCorner()) // Pawns can't move to corners
            return false;
        if (_board[b.y()][b.x()] != null) // Checking the destination is empty
            return false;

        // Checking the tiles the piece is moving through are empty.
        if (a.x() == b.x()) {   // the piece is moving in the Y axis
            for (int i = Math.min(a.y(), b.y()) + 1; i < Math.max(a.y(), b.y()); i++) {
                if (_board[i][a.x()] != null) {
                    return false;
                }
            }
        } else { // the piece is moving in the X axis
            for (int i = Math.min(a.x(), b.x()) + 1; i < Math.max(a.x(), b.x()); i++) {
                if (_board[a.y()][i] != null) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Capture all the pieces that should be captured recording to the rules of the game.
     * @param b the position of the piece that moved.
     * @return An array of the captured pieces.
     */
    private Pawn[] capture(Position b) {
        Pawn[] captured = new Pawn[4];
        if (!_board[b.y()][b.x()].isKing()) {
            for (int i = 0; i < 4; i++) { // check every direction of the pawn to see if it can capture
                int[] d = DIRECTIONS[i];
                int x = b.x() + d[0];
                int y = b.y() + d[1];
                // if there is an opponent's pawn
                if (inBounds(x, y) && _board[y][x] != null &&_board[y][x].getOwner() != _board[b.y()][b.x()].getOwner() && !_board[y][x].isKing()) {
                    x += d[0];
                    y += d[1];
                    // check the next tile in this location
                    if (!inBounds(x, y) || new Position(x,y).isCorner() || (_board[y][x] != null && _board[y][x].getType().equals(_board[b.y()][b.x()].getType()))) {
                        // if it is the end of the board or there is a friendly pawn
                        captured[i] = (new Pawn((Pawn)_board[y-d[1]][x-d[0]]));
                        _capturedPieces.add(captured[i]);
                        _board[y-d[1]][x-d[0]] = null;
                        ((Pawn)_board[b.y()][b.x()]).incrementKills();
                    }
                }
            }
        }
        return captured;
    }
    /**
     * Update the statistics of the game, that correspond to the move that was made, and to the piece that moved.
     * @param a the starting position of the piece.
     * @param b the destination position of the piece.
     * @param piece the piece that moved.
     * @param captured an array of the captured pieces.
     */

    private void updateStats(Position a, Position b, ConcretePiece piece, Pawn[] captured){

        _board[b.y()][b.x()].addMove(b); // Adding the move to the piece that moved
        _board[b.y()][b.x()].setDistance(_board[b.y()][b.x()].getDistance()+Math.abs(a.x()-b.x())+Math.abs(a.y()-b.y())); // Updating the distance of the piece that moved

        if (_board[b.y()][b.x()].getOwner() == _player1) // Updating the piecesPlaced array, that keeps count of pieces that been placed in each cell
            _piecesPlaced[b.y()][b.x()][_board[b.y()][b.x()].getSerial()-1]++;
        else
            _piecesPlaced[b.y()][b.x()][_board[b.y()][b.x()].getSerial()+12]++;

        if (piece.isKing()) //Enter a new turn to the turns stack, for the undo function
            _turns.push(new Turn(a, b, new King((King)piece), captured));
        else
            _turns.push(new Turn(a, b, new Pawn((Pawn)piece), captured));

    }

    /**
     * Get the piece located at a given position on the game board.
     *
     * @param position The position for which to retrieve the piece.
     * @return The piece at the specified position, or null if no piece is present.
     */
    @Override
    public Piece getPieceAtPosition(Position position) {
        return _board[position.y()][position.x()];
    }

    /**
     * Get the first player.
     *
     * @return The first player.
     */
    @Override
    public Player getFirstPlayer() {
        return _player1;
    }

    /**
     * Get the second player.
     *
     * @return The second player.
     */
    @Override
    public Player getSecondPlayer() {
        return _player2;
    }

    /**
     * Check if the game has finished, indicating whether a player has won or if it's a draw.
     *
     * @return true if the game has finished, false otherwise.
     */
    @Override
    public boolean isGameFinished() {

        // If the king is in one of the corners player 1 wins
        if (new Position(_kingPos[1],_kingPos[0]).isCorner()) {
            _player1.addWin();
            printStats(_player1);
            return true;
        }

        // If the king is surrounded from all 4 sides in enemy pawns or the board's border player 2 wins
        int count = 0;
        for (int[] d : DIRECTIONS) {
            int x = _kingPos[0] + d[0];
            int y = _kingPos[1] + d[1];
            if (!inBounds(x, y) || (_board[y][x] != null && !_board[y][x].getOwner().isPlayerOne()))
                count++;
        } if (count == 4) {
            _player2.addWin();
            printStats(_player2);
            return true;
        }

        return false;
    }

    /**
     * Print the statistics of the game, by the rules of the assignment.
     * @param winner the player that won the game.
     */
    private void printStats(ConcretePlayer winner) {
        // moves
        printMovesStats(winner);
        printStars();

        // kills
        printKillsStats(winner);
        printStars();

        // distances
        printDistancesStats(winner);
        printStars();

        // pieces
        printPiecesStats();
        printStars();
    }


    private void printStars() {
        for (int i = 0; i < 75; i++)
            System.out.print("*");
        System.out.println();
    }
    /**
     * Print the moves statistics of the game, by the rules of the assignment.
     * @param winner the player that won the game.
     */
    private void printMovesStats(ConcretePlayer winner) {
        ArrayList<ConcretePiece> moves = new ArrayList<>(37);
        for (ConcretePiece p : _capturedPieces) {
            if (p.getNumberOfMoves() > 1) { moves.add(p); }
        }
        for (int i = 0; i < _boardSize; i++) {
            for (int j = 0; j <_boardSize; j++) {
                if (_board[i][j] != null && _board[i][j].getNumberOfMoves() > 1)
                    moves.add(_board[i][j]);
            }
        }

        moves.sort(new MovesComp(winner));
        for (ConcretePiece piece : moves) {
            if (piece.getOwner().isPlayerOne() && !piece.isKing())
                System.out.print("D" + piece.getSerial() + ": [");
            else if (!piece.getOwner().isPlayerOne() && !piece.isKing())
                System.out.print("A" + piece.getSerial() + ": [");
            else
                System.out.print("K7" + ": [");

            int n = piece.getNumberOfMoves();
            for (int i = 0; i < n-1; i++)
                System.out.print("("+piece.getMoves(i).x()+", "+piece.getMoves(i).y()+"), ");
            System.out.print("("+piece.getMoves(n-1).x()+", "+piece.getMoves(n-1).y()+")]\n");
        }
    }
    /**
     * Print the kills statistics of the game, by the rules of the assignment.
     * @param winner the player that won the game.
     */
    private void printKillsStats(ConcretePlayer winner) {
        ArrayList<Pawn> kills = new ArrayList<>(37);
        for (ConcretePiece p : _capturedPieces)
            if (((Pawn)p).getKills() >= 1) { kills.add((Pawn)p); }
        for (int i = 0; i < _boardSize; i++) {
            for (int j = 0; j <_boardSize; j++) {
                if (_board[i][j] != null && !_board[i][j].isKing() && ((Pawn)_board[i][j]).getKills() >= 1)
                    kills.add((Pawn)_board[i][j]);
            }
        }

        kills.sort(new KillsComp(winner));
        for (Pawn piece : kills) {
            if (piece.getOwner().isPlayerOne())
                System.out.print("D" + piece.getSerial() + ": ");
            else
                System.out.print("A" + piece.getSerial() + ": ");
            System.out.print(piece.getKills() + " kills\n");
        }
    }
    /**
     * Print the distances statistics of the game, by the rules of the assignment.
     * @param winner the player that won the game.
     */
    private void printDistancesStats(ConcretePlayer winner) {
        ArrayList<ConcretePiece> distances = new ArrayList<>(37);
        for (ConcretePiece p : _capturedPieces)
            if (p.getDistance() >= 1) { distances.add(p); }
        for (int i = 0; i < _boardSize; i++) {
            for (int j = 0; j < _boardSize; j++) {
                if (_board[i][j] != null && _board[i][j].getDistance() >= 1)
                    distances.add(_board[i][j]);
            }
        }

        distances.sort(new DistanceComp(winner));
        for (ConcretePiece piece : distances) {
            if (piece.getOwner().isPlayerOne() && !piece.isKing())
                System.out.print("D" + piece.getSerial() + ": ");
            else if (!piece.getOwner().isPlayerOne())
                System.out.print("A" + piece.getSerial() + ": ");
            else
                System.out.print("K7: ");
            System.out.print(piece.getDistance() + " squares\n");
        }
    }
    /**
     * Print the tiles statistics of the game, by the rules of the assignment.
     */
    private void printPiecesStats() {
        LinkedList<Position> pieces = new LinkedList<>();
        for (int i = 0; i < _boardSize; i++) {
            for (int j = 0; j < _boardSize; j++) {
                int count = 0;
                for (int k = 0; k < 37; k++)
                    if (_piecesPlaced[i][j][k] != 0) { count++; }
                if (count >= 2) { pieces.add(new Position(j,i)); }
            }
        }

        pieces.sort(new PiecesComp(_piecesPlaced));
        for (Position position : pieces) {
            System.out.print("("+position.x()+", "+position.y()+")");
            int count = 0;
            for (int k = 0; k < 37; k++)
                if (_piecesPlaced[position.y()][position.x()][k] != 0) { count++; }
            System.out.println(count+" pieces");
        }
    }

    /**
     * Check if a given position is within the bounds of the game board.
     * @param x the row coordinate of the position.
     * @param y the col coordinate of the position.
     * @return true if the position is within the bounds of the game board, false otherwise.
     */
    private boolean inBounds(int x, int y) {
        return x >= 0 && x < _boardSize && y >= 0 && y < _boardSize;
    }

    /**
     * Check if it is currently the second player's turn.
     *
     * @return true if it's the second player's turn, false if it's the first player's turn.
     */
    @Override
    public boolean isSecondPlayerTurn() {
        return !_isPlayerOneTurn;
    }

    /**
     * Reset the game to its initial state, clearing the board and player information.
     */
    @Override
    public void reset() {
        _turns.clear();
        _kingPos[0] = 5;
        _kingPos[1] = 5;
        _isPlayerOneTurn = false; //player 2 - Attacker is starting
        _board = new ConcretePiece[_boardSize][_boardSize];
        initializeBoard(); // initialize the board - placing the pieces on board

        for (int i = 0; i < _boardSize; i++) { //initialize piecesPlaced Array that keeps count of pieces that been placed in each cell
            for (int j = 0; j < _boardSize; j++) {
                _piecesPlaced[i][j] = new int [37];
                if (_board[i][j] == null)
                    continue;
                if (_board[i][j].getOwner() == _player1)
                    _piecesPlaced[i][j][_board[i][j].getSerial()-1]++;
                else
                    _piecesPlaced[i][j][_board[i][j].getSerial()+12]++;
            }
        }
    }

    /**
     * Undo the last move made in the game, reverting the board state and turn order.
     */
    @Override
    public void undoLastMove() {
        if (_turns.isEmpty())
            return;
        _turns.pop().restore(_board, _kingPos, _piecesPlaced, _capturedPieces);
        _isPlayerOneTurn = !_isPlayerOneTurn;

    }

    /**
     * Get the size of the game board.
     *
     * @return The size of the game board, typically as the number of rows or columns.
     */
    @Override
    public int getBoardSize() {
        return _boardSize;
    }
    /**
     * Initialize the board, placing the pieces on the board.
     */
    private void initializeBoard(){
        // Defence Pieces
        _board[3][5] = new Pawn(_player1, 1,new Position(5,3));
        _board[7][5] = new Pawn(_player1, 13,new Position(5,7));
        for (int i = 4; i <= 6; i++) {
            _board[4][i] = new Pawn(_player1, i - 2, new Position(i,4));
            _board[6][i] = new Pawn(_player1, i + 6,new Position(i,6));
        }
        for (int i = 3; i <= 7; i++) {
            if (i == 5)
                _board[5][i] = new King(_player1,new Position(5,5));
            else
                _board[5][i] = new Pawn(_player1, i + 2,new Position(i,5));
        }

        // Attack pawns 1-6 and 19-24
        for (int i = 3, j = 20; i <= 7; i++, j++) {
            _board[0][i] = new Pawn(_player2, i - 2, new Position(i,0));
            _board[10][i] = new Pawn(_player2, j,new Position(i,10));
        }
        _board[1][5] = new Pawn(_player2, 6,new Position(5,1));
        _board[9][5] = new Pawn(_player2, 19,new Position(5,9));

        // Attack pawns 7-18
        for (int j = 3, k = 7, i = 8; j <= 7; j++, k += 2, i += 2) {
            _board[j][0] = new Pawn(_player2, k,new Position(0,j));
            _board[j][10] = new Pawn(_player2, i,new Position(10,j));
        }
        _board[5][1] = new Pawn(_player2, 12,new Position(1,5));
        _board[5][9] = new Pawn(_player2, 13,new Position(9,5));
        _board[6][0] = new Pawn(_player2, 15,new Position(0,6));
        _board[7][0] = new Pawn(_player2, 17,new Position(0,7));
        _board[5][10] = new Pawn(_player2, 14,new Position(10,5));
        _board[6][10] = new Pawn(_player2, 16,new Position(10,6));
        _board[7][10] = new Pawn(_player2, 18,new Position(10,7));
    }

}
