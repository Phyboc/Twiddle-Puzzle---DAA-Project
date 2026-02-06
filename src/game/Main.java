package game;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter board size N: ");
        int N = sc.nextInt();

        Board board = new Board(N);

        System.out.println("Choose mode:");
        System.out.println("1. Human vs Computer");
        System.out.println("2. Computer Only (step-by-step)");

        int mode = sc.nextInt();

        System.out.println("Choose AI:");
        System.out.println("1. BFS");
        System.out.println("2. A*");
        System.out.println("3. Divide & Conquer Greedy");

        int choice = sc.nextInt();

        Player computer;
        if (choice == 3) {
            computer = new ComputerPlayer2(board);
        } else {
            ComputerPlayer cp = new ComputerPlayer(board);
            cp.setAlgorithm(choice == 1);
            computer = cp;
        }

        if (mode == 1) {
            // Human vs Computer
            Player human = new HumanPlayer(board);
            new GameEngine(board, human, computer).startGame();
        } else {
            // Computer only, step-by-step
            runComputerOnly(board, computer, sc);
        }
    }

    private static void runComputerOnly(Board board, Player computer, Scanner sc) {

        System.out.println("\nInitial Board:");
        board.print();

        sc.nextLine(); // consume leftover newline

        while (!board.isSolved()) {

            System.out.print("\nPress ENTER for computer to make one move (or type q to quit): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("q")) break;

            int move = computer.getMove();

            System.out.println("Computer chooses move: " + move);
            board.executeMove(move);
            board.print();
        }

        if (board.isSolved()) {
            System.out.println("🎉 Puzzle Solved!");
        } else {
            System.out.println("Stopped by user.");
        }
    }
}
