package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ComputerPlayer2 extends AbstractPlayer {
    int finalgrid[][];
    private Map<String, Integer> bestH = new HashMap<>(); //this is for dominance pruning, 
                                                // it stores the best heuristic value for a given state

    public ComputerPlayer2(Board board) {
        super(board);
        this.finalgrid = new int[board.getGrid().length][board.getGrid()[0].length];
        int val = 1;
        for (int i = 0; i<finalgrid.length; i++){
            for (int j = 0; j<finalgrid[0].length; j++){
                finalgrid[i][j] = val++;
            }
        }
    }
    //these are to override the methods in abstract player or more like player interface
    @Override
    public int getMove() {
        return solvewithdc();
    }
    @Override
    public String getName() {
        return "Divide and Conquer using Greedy Heuristic";
    }

    //this is to store the direction sum and the element number
    // static class Element{
    //     int num;
    //     String dir;
    //     Element(int num, String dir){
    //         this.num = num;
    //         this.dir = dir;
    //     }
    //     Element(int num){
    //         this.num = num;
    //         this.dir = "";
    //     }
    // }

    //this is to store the rotation number and the rows & columns that rotate
    static class Rotator{
        int r1, r2, c1, c2;
        int h;
        int moveno;
        Rotator(int r1, int r2, int c1, int c2,int h){
            this.r1 = r1;
            this.r2 = r2;
            this.c1 = c1;
            this.c2 = c2;
            this.h = h;
        }
    }

    private int solvewithdc(){
        bestH.clear();   
        int[][] grid = board.getGrid();
        return rotation(grid, 0, grid.length-1, 0, grid[0].length-1).moveno;
        
    }

    //this method recursively divides the nxn matrix till it is 2x2 matrix, initializes the rotator class 
    // and also calculates heuristics and returns the rotator class var that has max heuristics.    
    private Rotator rotation(int[][] matrix, int s1, int s2, int e1, int e2){
        if (s1 >= s2 || e1 >= e2) return null;
        if (((s2 - s1) == 1) && ((e2 - e1) == 1)) {
            String key = encode(matrix);
            int[][] next = rotate(matrix, s1, s2, e1, e2);
            int denom = h2(next, s1,s2,e1,e2);
            int h = (denom == 0) ? Integer.MAX_VALUE : h1(next, s1,s2,e1,e2) / denom;
            //dominance pruning is done here
            key = encode(next);
            if (bestH.containsKey(key) && bestH.get(key) >= h)
                return null;

            bestH.put(key, h);
            Rotator rotator = new Rotator(s1,s2,e1,e2,h);
            rotator.moveno = getMoveno(matrix, s1,s2,e1,e2);
            return rotator;
        }
        //Move ordering is done here
        List<Rotator> children = new ArrayList<>();
        children.add(rotation(matrix, s1, s2-1, e1, e2-1));
        children.add(rotation(matrix, s1+1, s2, e1, e2-1));
        children.add(rotation(matrix, s1, s2-1, e1+1, e2));
        children.add(rotation(matrix, s1+1, s2, e1+1, e2));
        children.removeIf(Objects::isNull);
        children.sort((a, b) -> b.h - a.h);
        return children.isEmpty() ? null : children.get(0);
    }

    //this method is to calculate the move number based on the rows and columns that are rotating
    private int getMoveno(int[][] matrix, int s1, int s2, int e1, int e2){
        int cols = matrix[0].length;
        int moveNo = s1 * cols + e1 + 1; 
        return moveNo;
    }

    //this method is to calculate the heuristic 1 which is the direction value sum
    private int h1(int[][] matrix, int s1, int s2, int e1, int e2){
        int[][] copy = rotate(matrix, s1, s2, e1, e2);
        int sum = 0;
        for (int i = s1; i<=s2; i++){
            for (int j = e1; j<=e2; j++){
                sum += Math.abs(copy[i][j] - finalgrid[i][j]);
            }
        }
        return sum;
    }

    //this method is to calculate the heuristic 2 which is the 
    // number of misplaced elements in the in full matrix
    private int h2(int[][] matrix, int s1, int s2, int e1, int e2){
        int[][] copy = rotate(matrix, s1, s2, e1, e2);
        int count = 0;
        int val = 1;
        for (int i = 0; i<=matrix.length-1; i++){
            for (int j = 0; j<=matrix[0].length-1; j++){
                if (copy[i][j] != val++) count++;
            }
        }
        return count;
    }

    //this matrix is to rotate any matrix given the rows and columns number
    private int[][] rotate(int[][] matrix, int r1, int r2, int c1, int c2) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, cols);
        }
        copy[r1][c1] = matrix[r1][c2];
        copy[r2][c1] = matrix[r1][c1];
        copy[r2][c2] = matrix[r2][c1];
        copy[r1][c2] = matrix[r2][c2];
        return copy;
}

    //this matrix is to get the max heuristics among the 4 possible rotations of a matrix 
    // and return the rotator class var that has max heuristics
    // private Rotator max(Rotator a, Rotator b, Rotator c, Rotator d){
    //     Rotator max = a;
    //     if (b.h > max.h) max = b;
    //     if (c.h > max.h) max = c;
    //     if (d.h > max.h) max = d;
    //     return max;
    // }

    //this method is to encode the matrix into a string format for storing in the visited set for pruning
    private String encode(int[][] g) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : g)
            for (int v : row)
                sb.append(v).append(',');
        return sb.toString();
    }

    //this method is to check if the matrix is solved or not by comparing it with the final grid
    // private boolean isSolved(int[][] g) {
    //     int v = 1;
    //     for (int i = 0; i < g.length; i++)
    //         for (int j = 0; j < g[0].length; j++)
    //             if (g[i][j] != v++) return false;
    //     return true;
    // }
}