package game;
import java.util.*;

public interface Player {
    int getMove();
    String getName();
}
abstract class AbstractPlayer implements Player {
    protected Board board;

    AbstractPlayer(Board board) {
        this.board = board;
    }
}


