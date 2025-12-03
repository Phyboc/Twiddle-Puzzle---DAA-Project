package game;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Board board = new Board();

        Player human = new HumanPlayer(board);
        Player computer = new ComputerPlayer(board);

        GameEngine engine = new GameEngine(board, human, computer);
        engine.startGame();
    }
}
