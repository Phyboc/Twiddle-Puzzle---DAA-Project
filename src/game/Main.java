package game;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter board size N: ");
        int N = sc.nextInt();

        Board board = new Board(N);

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

        Player human = new HumanPlayer(board);
        new GameEngine(board, human, computer).startGame();
    }

}
