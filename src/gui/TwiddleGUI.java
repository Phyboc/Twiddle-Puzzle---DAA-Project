package gui;

import game.*;
import java.awt.*;
import javax.swing.*;

public class TwiddleGUI extends JFrame {

    private Board board;
    private JLabel[][] cells = new JLabel[3][3];
    private Player computer;

    private JRadioButton aStarBtn;
    private JRadioButton bfsBtn;
    private JLabel moveLabel;

    public TwiddleGUI() {
        board = new Board();
        computer = new ComputerPlayer(board);

        setTitle("Twiddle Puzzle");
        setSize(420, 560);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupGrid();
        setupControls();
        refreshBoard();
        updateMoveCount();

        setVisible(true);
    }

    // ---------------- GRID ----------------
    private void setupGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        gridPanel.setBounds(60, 30, 300, 300);

        Font font = new Font("Arial", Font.BOLD, 28);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(font);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cells[i][j] = label;
                gridPanel.add(label);
            }
        }
        add(gridPanel);
    }

    // ---------------- CONTROLS ----------------
    private void setupControls() {

        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setBounds(40, 340, 80, 20);
        add(algoLabel);

        aStarBtn = new JRadioButton("A*", true);
        bfsBtn = new JRadioButton("BFS");

        aStarBtn.setBounds(110, 340, 100, 20);
        bfsBtn.setBounds(220, 340, 120, 20);

        ButtonGroup group = new ButtonGroup();
        group.add(aStarBtn);
        group.add(bfsBtn);

        add(aStarBtn);
        add(bfsBtn);

        moveLabel = new JLabel("Moves: 0");
        moveLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moveLabel.setBounds(150, 360, 150, 25);
        add(moveLabel);

        int y = 390;
        JButton b1 = new JButton("1");
        JButton b2 = new JButton("2");
        JButton b3 = new JButton("3");
        JButton b4 = new JButton("4");

        b1.setBounds(40, y, 80, 40);
        b2.setBounds(125, y, 80, 40);
        b3.setBounds(210, y, 80, 40);
        b4.setBounds(295, y, 80, 40);

        add(b1);
        add(b2);
        add(b3);
        add(b4);

        JButton computerBtn = new JButton("Computer Move");
        JButton randomBtn = new JButton("Randomize");

        computerBtn.setBounds(40, y + 50, 335, 40);
        randomBtn.setBounds(40, y + 100, 335, 40);

        add(computerBtn);
        add(randomBtn);

        b1.addActionListener(e -> doMove(1));
        b2.addActionListener(e -> doMove(2));
        b3.addActionListener(e -> doMove(3));
        b4.addActionListener(e -> doMove(4));

        computerBtn.addActionListener(e -> computerMove());

        randomBtn.addActionListener(e -> {
            board.randomize();
            refreshBoard();
            updateMoveCount();
        });
    }

    // ---------------- MOVES ----------------
    private void doMove(int move) {
        animateRotation(move, () -> {
            board.executeMove(move);
            refreshBoard();
            updateMoveCount();
            checkSolved();
        });
    }

    private void computerMove() {
        ((ComputerPlayer) computer).setAlgorithm(bfsBtn.isSelected());

        int move = computer.getMove();
        animateRotation(move, () -> {
            board.executeMove(move);
            refreshBoard();
            updateMoveCount();
            checkSolved();
        });
    }

    // ---------------- ANIMATION ----------------
    private void animateRotation(int mv, Runnable after) {

        int[][] M = switch (mv) {
            case 1 -> new int[][]{{0,0},{1,0},{1,1},{0,1}};
            case 2 -> new int[][]{{1,0},{2,0},{2,1},{1,1}};
            case 3 -> new int[][]{{1,1},{2,1},{2,2},{1,2}};
            default -> new int[][]{{0,1},{1,1},{1,2},{0,2}};
        };

        // Save values
        String[] v = new String[4];
        for (int i = 0; i < 4; i++)
            v[i] = cells[M[i][0]][M[i][1]].getText();

        // Highlight
        for (int[] c : M)
            cells[c[0]][c[1]].setBorder(
                    BorderFactory.createLineBorder(Color.RED, 3));

        Timer t = new Timer(120, null);
        t.addActionListener(e -> {
            // Clockwise visual rotation
            cells[M[0][0]][M[0][1]].setText(v[3]);
            cells[M[1][0]][M[1][1]].setText(v[0]);
            cells[M[2][0]][M[2][1]].setText(v[1]);
            cells[M[3][0]][M[3][1]].setText(v[2]);

            t.stop();

            // Reset borders
            for (int[] c : M)
                cells[c[0]][c[1]].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2));

            after.run();
        });
        t.start();
    }

    // ---------------- UI HELPERS ----------------
    private void refreshBoard() {
        int[][] g = board.getGrid();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                cells[i][j].setText(String.valueOf(g[i][j]));
    }

    private void updateMoveCount() {
        moveLabel.setText("Moves: " + board.getMoves());
    }

    private void checkSolved() {
        if (board.isSolved()) {
            JOptionPane.showMessageDialog(this, "🎉 Puzzle Solved!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TwiddleGUI::new);
    }
}

















//package gui;
//
//import game.*;
//import java.awt.*;
//import javax.swing.*;
//
//public class TwiddleGUI extends JFrame {
//
//    private Board board;
//    private JLabel[][] cells = new JLabel[3][3];
//    private Player computer;
//
//    private JRadioButton aStarBtn;
//    private JRadioButton bfsBtn;
//    private JLabel moveLabel;
//
//    public TwiddleGUI() {
//        board = new Board();
//        computer = new ComputerPlayer(board);
//
//        setTitle("Twiddle Puzzle");
//        setSize(420, 560);
//        setLayout(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        setupGrid();
//        setupControls();
//        refreshBoard();
//        updateMoveCount();
//
//        setVisible(true);
//    }
//
//    private void setupGrid() {
//        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
//        gridPanel.setBounds(60, 30, 300, 300);
//
//        Font font = new Font("Arial", Font.BOLD, 28);
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                JLabel label = new JLabel("", SwingConstants.CENTER);
//                label.setFont(font);
//                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//                cells[i][j] = label;
//                gridPanel.add(label);
//            }
//        }
//        add(gridPanel);
//    }
//
//    private void setupControls() {
//
//        JLabel algoLabel = new JLabel("Algorithm:");
//        algoLabel.setBounds(40, 340, 80, 20);
//        add(algoLabel);
//
//        aStarBtn = new JRadioButton("A*", true);
//        bfsBtn = new JRadioButton("BFS");
//
//        aStarBtn.setBounds(110, 340, 100, 20);
//        bfsBtn.setBounds(220, 340, 120, 20);
//
//        ButtonGroup group = new ButtonGroup();
//        group.add(aStarBtn);
//        group.add(bfsBtn);
//
//        add(aStarBtn);
//        add(bfsBtn);
//
//        moveLabel = new JLabel("Moves: 0");
//        moveLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        moveLabel.setBounds(150, 360, 150, 25);
//        add(moveLabel);
//
//        int y = 390;
//        JButton b1 = new JButton("1");
//        JButton b2 = new JButton("2");
//        JButton b3 = new JButton("3");
//        JButton b4 = new JButton("4");
//
//        b1.setBounds(40, y, 80, 40);
//        b2.setBounds(125, y, 80, 40);
//        b3.setBounds(210, y, 80, 40);
//        b4.setBounds(295, y, 80, 40);
//
//        add(b1);
//        add(b2);
//        add(b3);
//        add(b4);
//
//        JButton computerBtn = new JButton("Computer Move");
//        JButton randomBtn = new JButton("Randomize");
//
//        computerBtn.setBounds(40, y + 50, 335, 40);
//        randomBtn.setBounds(40, y + 100, 335, 40);
//
//        add(computerBtn);
//        add(randomBtn);
//
//        b1.addActionListener(e -> playerMove(1));
//        b2.addActionListener(e -> playerMove(2));
//        b3.addActionListener(e -> playerMove(3));
//        b4.addActionListener(e -> playerMove(4));
//
//        computerBtn.addActionListener(e -> computerMove());
//
//        randomBtn.addActionListener(e -> {
//            board.randomize();
//            refreshBoard();
//            updateMoveCount();
//        });
//    }
//
//    private void playerMove(int move) {
//        highlightRotation(move);
//        board.executeMove(move);
//        refreshBoard();
//        updateMoveCount();
//        checkSolved();
//    }
//
//    private void computerMove() {
//        boolean useBFS = bfsBtn.isSelected();
//        ((ComputerPlayer) computer).setAlgorithm(useBFS);
//
//        int move = computer.getMove();
//        highlightRotation(move);
//        board.executeMove(move);
//        refreshBoard();
//        updateMoveCount();
//        checkSolved();
//    }
//
//    private void refreshBoard() {
//        int[][] g = board.getGrid();
//        for (int i = 0; i < 3; i++)
//            for (int j = 0; j < 3; j++)
//                cells[i][j].setText(String.valueOf(g[i][j]));
//    }
//
//    private void updateMoveCount() {
//        moveLabel.setText("Moves: " + board.getMoves());
//    }
//
//    private void checkSolved() {
//        if (board.isSolved()) {
//            JOptionPane.showMessageDialog(this, "🎉 Puzzle Solved!");
//        }
//    }
//
//    private void highlightRotation(int mv) {
//        int[][] M = switch (mv) {
//            case 1 -> new int[][]{{0,0},{1,0},{1,1},{0,1}};
//            case 2 -> new int[][]{{1,0},{2,0},{2,1},{1,1}};
//            case 3 -> new int[][]{{1,1},{2,1},{2,2},{1,2}};
//            default -> new int[][]{{0,1},{1,1},{1,2},{0,2}};
//        };
//
//        for (int[] c : M)
//            cells[c[0]][c[1]].setBorder(
//                BorderFactory.createLineBorder(Color.RED, 3));
//
//        new Timer(250, e -> {
//            for (int[] c : M)
//                cells[c[0]][c[1]].setBorder(
//                    BorderFactory.createLineBorder(Color.BLACK, 2));
//        }).start();
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(TwiddleGUI::new);
//    }
//}