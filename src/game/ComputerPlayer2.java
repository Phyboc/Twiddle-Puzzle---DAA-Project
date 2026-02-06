package game;

public class ComputerPlayer2 extends AbstractPlayer {
    int finalgrid[][];
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
    @Override
    public int getMove() {
        return solvewithdc();
    }
    @Override
    public String getName() {
        return "Divide and Conquer using Greedy Heuristic";
    }
    static class Element{
        int num;
        String dir;
        Element(int num, String dir){
            this.num = num;
            this.dir = dir;
        }
        Element(int num){
            this.num = num;
            this.dir = "";
        }
    }
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
        int[][] grid = board.getGrid();
        return rotation(grid, 0, grid.length-1, 0, grid[0].length-1).moveno;
        
    }
    private Rotator rotation(int[][] matrix, int s1, int s2, int e1, int e2){
        if (s1 >= s2 || e1 >= e2) return null;
        if (((s2 - s1) == 1) && ((e2 - e1) == 1)) {
            int denom = h2(matrix, s1,s2,e1,e2);
            int h = (denom == 0) ? Integer.MAX_VALUE : h1(matrix, s1,s2,e1,e2) / denom;
            Rotator rotator = new Rotator(s1,s2,e1,e2,h);
            rotator.moveno = getMoveno(matrix, s1,s2,e1,e2);
            return rotator;
        }
        return max(rotation(matrix, s1, s2-1, e1, e2-1),rotation(matrix, s1+1, s2, e1, e2-1),rotation(matrix, s1, s2-1, e1+1, e2),rotation(matrix, s1+1, s2, e1+1, e2));
    }
    private int getMoveno(int[][] matrix, int s1, int s2, int e1, int e2){
        int cols = matrix[0].length;
        int moveNo = s1 * cols + e1 + 1; 
        return moveNo;
    }
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
    private Rotator max(Rotator a, Rotator b, Rotator c, Rotator d){
        Rotator max = a;
        if (b.h > max.h) max = b;
        if (c.h > max.h) max = c;
        if (d.h > max.h) max = d;
        return max;
    }
    private boolean isSolved(int[][] g) {
        int v = 1;
        for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[0].length; j++)
                if (g[i][j] != v++) return false;
        return true;
    }
}