package game;
import java.util.*;

public class GameEngine {
    private final Board board;
    private final Player human;
    private final Player computer;

    public GameEngine(Board board, Player human, Player computer) {
        this.board = board;
        this.human = human;
        this.computer = computer;
    }

    public void startGame() {
        System.out.println("Starting Puzzle...\n");

        board.print();

        while (!board.isSolved()) {

            System.out.println("*** Human's turn ***");
            board.executeMove(human.getMove());
            if (board.isSolved()) break;

            System.out.println("*** Computer's turn ***");
            board.executeMove(computer.getMove());
        }

        System.out.println("🎉 Puzzle Solved!");
    }
}
