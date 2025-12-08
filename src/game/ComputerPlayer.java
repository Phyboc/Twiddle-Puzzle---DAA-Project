package game;

import java.util.*;

public class ComputerPlayer extends AbstractPlayer {

    public ComputerPlayer(Board board) {
        super(board);
    }

    private static class State {
        int[][] grid;
        int g;                 
        int h;                 
        int f;                 
        int move;              
        State parent;

        State(int[][] grid, int g, int move, State parent) {
            this.grid = grid;
            this.g = g;
            this.h = heuristic(grid);
            this.f = g + h;
            this.move = move;
            this.parent = parent;
        }
    }

    
    public int getMove() {
        return solveWithAStar();
    }

    private int solveWithAStar() {

        PriorityQueue<State> open = new PriorityQueue<>(Comparator.comparingInt(s -> s.f));
        Map<String, Integer> closed = new HashMap<>();

        int[][] start = copy(board.getGrid());
        State startState = new State(start, 0, -1, null);
        open.add(startState);

        while (!open.isEmpty()) {

            State cur = open.poll();
            String key = encode(cur.grid);

            if (closed.containsKey(key) && closed.get(key) <= cur.g) continue;
            closed.put(key, cur.g);

            if (isSolved(cur.grid)) {
                return extractFirstMove(cur);
            }

            for (int m = 1; m <= 4; m++) {
                int[][] next = rotate(cur.grid, m);
                State nextState = new State(next, cur.g + 1, m, cur);
                open.add(nextState);
            }
        }

        return 1;  
    }

    private static int heuristic(int[][] g) {
        int count = 0;
        int v = 1;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (g[i][j] != v++) count++;
        return (int) Math.ceil(count / 4.0);
    }

    private int[][] rotate(int[][] src, int mvnum) {
        int[][] g = copy(src);

        int[][] M = switch (mvnum) {
            case 1 -> new int[][]{{0,0},{1,0},{1,1},{0,1}};
            case 2 -> new int[][]{{1,0},{2,0},{2,1},{1,1}};
            case 3 -> new int[][]{{1,1},{2,1},{2,2},{1,2}};
            default -> new int[][]{{0,1},{1,1},{1,2},{0,2}};
        };

        int a1 = M[0][0], b1 = M[0][1];
        int a2 = M[1][0], b2 = M[1][1];
        int a3 = M[2][0], b3 = M[2][1];
        int a4 = M[3][0], b4 = M[3][1];

        int v1 = g[a1][b1];
        int v2 = g[a2][b2];
        int v3 = g[a3][b3];

        g[a1][b1] = g[a4][b4];
        g[a2][b2] = v1;
        g[a3][b3] = v2;
        g[a4][b4] = v3;

        return g;
    }

    private int[][] copy(int	[][] src) {
        int[][] c = new int[3][3];
        for (int i = 0; i < 3; i++)
            c[i] = src[i].clone();
        return c;
    }

    private boolean isSolved(int[][] g) {
        int v = 1;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (g[i][j] != v++) return false;
        return true;
    }

    private int extractFirstMove(State s) {
        while (s.parent.parent != null)
            s = s.parent;
        return s.move;
    }

    private String encode(int[][] g) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : g)
            for (int x : row)
                sb.append(x);
        return sb.toString();
    }

    public String getName() {
        return "A* Computer";
    }
}
