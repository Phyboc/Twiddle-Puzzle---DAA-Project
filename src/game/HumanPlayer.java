package game;
import java.util.*;

public class HumanPlayer extends AbstractPlayer {

    private final Scanner sc = new Scanner(System.in);

    public HumanPlayer(Board board) {
        super(board);
    }
    public int getMove() {
        System.out.print("\nEnter your move number (1-4): ");
        int mv = sc.nextInt();

        while (mv < 1 || mv > 4) {
            System.out.print("Invalid move. Enter again (1-4): ");
            mv = sc.nextInt();
        }
        return mv;
    }
    public String getName() {
        return "Human";
    }
}
