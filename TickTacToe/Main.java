package TickTacToe;

import java.util.Random;

enum Piece {
    ZERO,
    CROSS
}

abstract class Strategy {}

abstract class AIStrategy extends Strategy {
    abstract String getDifficultyName();
}

class HardStrategy extends AIStrategy {
    @Override
    String getDifficultyName() { return "Hard"; }
}

class MediumStrategy extends AIStrategy {
    @Override
    String getDifficultyName() { return "Medium"; }
}

class EasyStrategy extends AIStrategy {
    @Override
    String getDifficultyName() { return "Easy"; }
}

class Human extends Strategy {}

class AI extends Strategy {
    private AIStrategy difficulty;

    public AI setDifficulty(AIStrategy strategy) {
        this.difficulty = strategy;
        return this;
    }

    public AIStrategy getDifficulty() {
        return this.difficulty;
    }
}

class Player {
    private Piece piece;
    private Strategy strategy;

    public Player piece(Piece piece) {
        this.piece = piece;
        return this;
    }

    public Player strategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public Piece getPiece() {
        return piece;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}

class Board {
    private int[][] grid = new int[3][3];

    public Board() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j] = -1;
    }

    public boolean mark(Piece piece, int row, int col) {
        if (row < 0 || col < 0 || row >= 3 || col >= 3) return false;
        if (grid[row][col] != -1) return false;
        grid[row][col] = (piece == Piece.ZERO) ? 0 : 1;
        return true;
    }

    public boolean isFull() {
        for (int[] row : grid)
            for (int cell : row)
                if (cell == -1) return false;
        return true;
    }

    public int checkWinner() {

        for (int i = 0; i < 3; i++) {
            if (grid[i][0] != -1 && grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2])
                return grid[i][0];
            if (grid[0][i] != -1 && grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i])
                return grid[0][i];
        }

        if (grid[0][0] != -1 && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
            return grid[0][0];
        if (grid[0][2] != -1 && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])
            return grid[0][2];

        return -1;
    }

    public void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char c = (grid[i][j] == -1) ? '-' : (grid[i][j] == 0 ? 'O' : 'X');
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class BoardManager {
    Board board = new Board();

    public boolean mark(Piece piece, int row, int col) {
        return board.mark(piece, row, col);
    }

    public int checkWinner() {
        return board.checkWinner();
    }

    public boolean isFull() {
        return board.isFull();
    }

    public void printBoard() {
        board.printBoard();
    }
}

interface GameState {
    void start(Game game);
    void move(Game game, int row, int col);
    void pause(Game game);
    void resume(Game game);
}

class NewGame implements GameState {
    @Override
    public void start(Game game) {
        System.out.println("Starting new game...");
        game.setState(new Playing());
    }

    @Override
    public void move(Game game, int row, int col) {
        System.out.println("Game hasn't started yet!");
    }

    @Override
    public void pause(Game game) {
        System.out.println("Can't pause before starting!");
    }

    @Override
    public void resume(Game game) {
        System.out.println("Game not started yet!");
    }
}

class Playing implements GameState {
    @Override
    public void start(Game game) {
        System.out.println("Game already in progress!");
    }

    @Override
    public void move(Game game, int row, int col) {
        Player current = game.getCurrentPlayer();
        boolean marked;

        if (current.getStrategy() instanceof Human) {
            marked = game.boardManager.mark(current.getPiece(), row, col);
            if (!marked) {
                System.out.println("Invalid move!");
                return;
            }
        } else if (current.getStrategy() instanceof AI ai) {
            System.out.println("AI (" + ai.getDifficulty().getDifficultyName() + ") is thinking...");
            Random r = new Random();
            do {
                row = r.nextInt(3);
                col = r.nextInt(3);
            } while (!game.boardManager.mark(current.getPiece(), row, col));
        } else {
            return;
        }

        game.boardManager.printBoard();

        int winner = game.boardManager.checkWinner();
        if (winner != -1) {
            System.out.println((winner == 0 ? "ZERO" : "CROSS") + " wins!");
            game.setState(new Finished());
            return;
        }

        if (game.boardManager.isFull()) {
            System.out.println("It's a draw!");
            game.setState(new Finished());
            return;
        }

        // Switch turn
        game.switchPlayer();
    }

    @Override
    public void pause(Game game) {
        System.out.println("Game paused.");
        game.setState(new Paused());
    }

    @Override
    public void resume(Game game) {
        System.out.println("Already playing!");
    }
}

class Paused implements GameState {
    @Override
    public void start(Game game) {
        System.out.println("Game is already started but paused!");
    }

    @Override
    public void move(Game game, int row, int col) {
        System.out.println("Game is paused. Resume first!");
    }

    @Override
    public void pause(Game game) {
        System.out.println("Already paused!");
    }

    @Override
    public void resume(Game game) {
        System.out.println("Resuming game...");
        game.setState(new Playing());
    }
}

class Finished implements GameState {
    @Override
    public void start(Game game) {
        System.out.println("Restarting game...");
        game.reset();
        game.setState(new Playing());
    }

    @Override
    public void move(Game game, int row, int col) {
        System.out.println("Game finished. Start again to play new match.");
    }

    @Override
    public void pause(Game game) {
        System.out.println("Can't pause a finished game.");
    }

    @Override
    public void resume(Game game) {
        System.out.println("Can't resume a finished game.");
    }
}

class Game {
    BoardManager boardManager = new BoardManager();
    private GameState currentState;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Game(Piece piece, Strategy opponent) {
        this.player1 = new Player().piece(piece).strategy(new Human());
        this.player2 = new Player()
            .piece(piece == Piece.ZERO ? Piece.CROSS : Piece.ZERO)
            .strategy(opponent);
        this.currentPlayer = player1;
        this.currentState = new NewGame();
    }

    public void setState(GameState state) {
        this.currentState = state;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == player1) ? player2 : player1;
    }

    public void reset() {
        this.boardManager = new BoardManager();
        this.currentPlayer = player1;
    }

    public void start() { currentState.start(this); }
    public void move(int row, int col) { currentState.move(this, row, col); }
    public void pause() { currentState.pause(this); }
    public void resume() { currentState.resume(this); }
}

public class Main {
    public static void main(String[] args) {
        Game game = new Game(Piece.ZERO, new AI().setDifficulty(new HardStrategy()));

        game.move(0, 0);
        game.start();
        game.move(0, 0);
        game.move(1, 1);
        game.pause();
        game.move(2, 2);
        game.resume();
        game.move(0, 1);
        game.move(2, 2);
    }
}