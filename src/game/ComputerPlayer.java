package game;

import java.util.*;

public class ComputerPlayer extends AbstractPlayer {

    private final Random rand = new Random();

    public ComputerPlayer(Board board) {
        super(board);
    }

    public int getMove() {
        int mv = rand.nextInt(4) + 1;
        System.out.println("Computer chooses move: " + mv);
        return mv;
    }
    public String getName() {
        return "Computer";
    }
}