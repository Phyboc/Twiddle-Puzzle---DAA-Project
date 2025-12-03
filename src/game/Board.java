package game;

import java.util.*;

public class Board {
    private int[][] grid;
    private int moves = 0;

    private static final int[][] TARGET = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    public Board() {
        generateRandomBoard();
    }

    private void generateRandomBoard() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) numbers.add(i);

        Collections.shuffle(numbers);

        grid = new int[3][3];
        int index = 0;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                grid[i][j] = numbers.get(index++);
    }

    public boolean isSolved() {
        return Arrays.deepEquals(grid, TARGET);
    }

    public void executeMove(int mvnum) {
        int[][] M = switch (mvnum) {
            case 1 -> new int[][]{{0,0}, {1,0}, {1,1}, {0,1}};
            case 2 -> new int[][]{{1,0}, {2,0}, {2,1}, {1,1}};
            case 3 -> new int[][]{{1,1}, {2,1}, {2,2}, {1,2}};
            default -> new int[][]{{0,1}, {1,1}, {1,2}, {0,2}};
        };

        rotate(M);
        moves++;
        print();
    }

    private void rotate(int[][] c) {
        int a1 = c[0][0], b1 = c[0][1];
        int a2 = c[1][0], b2 = c[1][1];
        int a3 = c[2][0], b3 = c[2][1];
        int a4 = c[3][0], b4 = c[3][1];

        int v1 = grid[a1][b1];
        int v2 = grid[a2][b2];
        int v3 = grid[a3][b3];

        grid[a1][b1] = grid[a4][b4];
        grid[a2][b2] = v1;
        grid[a3][b3] = v2;
        grid[a4][b4] = v3;
    }

    public void print() {
        for (int[] row : grid)
            System.out.println(Arrays.toString(row));
        System.out.println("Moves: " + moves + "\n");
    }
    public void randomize() {
        generateRandomBoard();
        print();
    }
    public int[][] getGrid() {
        return grid;
    }

}
