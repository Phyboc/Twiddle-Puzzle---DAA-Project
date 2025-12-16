package game;

import java.util.*;

public class ComputerPlayer extends AbstractPlayer {

    private boolean useBFS = false; // Default to A*

    public ComputerPlayer(Board board) {
        super(board);
    }

    // --- The Switch ---
    public void setAlgorithm(boolean useBFS) {
        this.useBFS = useBFS;
    }

    public boolean isUsingBFS() {
        return useBFS;
    }

    // --- The Brain ---
    public int getMove() {
        if (useBFS) {
            return solveWithBFS();
        } else {
            return solveWithAStar();
        }
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

    
    private int solveWithBFS() {
        
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        int[][] start = copy(board.getGrid());
        
        State startState = new State(start, 0, -1, null); 
        
        queue.add(startState);
        visited.add(encode(start));

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            if (isSolved(cur.grid)) {
                return extractFirstMove(cur);
            }

            for (int m = 1; m <= 4; m++) {
                int[][] next = rotate(cur.grid, m);
                String key = encode(next);

                if (!visited.contains(key)) {
                    visited.add(key);
                    
                    queue.add(new State(next, 0, m, cur));
                }
            }
        }
        return 1;
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
            this.move = move;
            this.parent = parent;
            
            
            this.h = heuristic(grid);
            this.f = g + h;
        }
    }

    private static int heuristic(int[][] g) {
        int count = 0;
        int v = 1;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (g[i][j] != v++) count++;
        return (int) Math.ceil(count / 4.0);
    }

    private int extractFirstMove(State s) {
        
        while (s.parent != null && s.parent.parent != null)
            s = s.parent;
        return s.move;
    }

    private boolean isSolved(int[][] g) {
        int v = 1;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (g[i][j] != v++) return false;
        return true;
    }

    private int[][] rotate(int[][] src, int mvnum) {
        int[][] g = copy(src);
        int[][] M = switch (mvnum) {
            case 1 -> new int[][]{{0,0},{1,0},{1,1},{0,1}};
            case 2 -> new int[][]{{1,0},{2,0},{2,1},{1,1}};
            case 3 -> new int[][]{{1,1},{2,1},{2,2},{1,2}};
            default -> new int[][]{{0,1},{1,1},{1,2},{0,2}};
        };

        int v1 = g[M[0][0]][M[0][1]];
        int v2 = g[M[1][0]][M[1][1]];
        int v3 = g[M[2][0]][M[2][1]];

        g[M[0][0]][M[0][1]] = g[M[3][0]][M[3][1]];
        g[M[1][0]][M[1][1]] = v1;
        g[M[2][0]][M[2][1]] = v2;
        g[M[3][0]][M[3][1]] = v3;
        return g;
    }

    private int[][] copy(int[][] src) {
        int[][] c = new int[3][3];
        for (int i = 0; i < 3; i++)
            c[i] = src[i].clone();
        return c;
    }

    private String encode(int[][] g) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : g) for (int x : row) sb.append(x);
        return sb.toString();
    }

    public String getName() {
        return useBFS ? "BFS Computer" : "A* Computer";
    }
}
