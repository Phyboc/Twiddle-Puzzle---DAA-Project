package gui;

import game.*;
import javax.swing.*;
import java.awt.*;

public class TwiddleGUI extends JFrame {

    private Board board;
    private JLabel[][] cells = new JLabel[3][3];
    private Player human;
    private Player computer; // Just one computer player now!

    private JRadioButton aStarBtn;
    private JRadioButton bfsBtn;

    public TwiddleGUI() {
        board = new Board();
        human = new HumanPlayer(board);
        computer = new ComputerPlayer(board); // Initialize once

        setTitle("Twiddle Puzzle");
        setSize(420, 560);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupGrid();
        setupControls();
        refreshBoard();

        setVisible(true);
    }

    private void setupGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        gridPanel.setBounds(60, 30, 300, 300);

        Font f = new Font("Arial", Font.BOLD, 28);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(f);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cells[i][j] = label;
                gridPanel.add(label);
            }
        }
        add(gridPanel);
    }

    private void setupControls() {
        
        // --- Algorithm Selection Radio Buttons ---
        JLabel lblAlgo = new JLabel("AI Logic:");
        lblAlgo.setBounds(40, 340, 80, 20);
        add(lblAlgo);

        aStarBtn = new JRadioButton("A* (Fast)", true);
        bfsBtn = new JRadioButton("BFS (Wide)");
        
        aStarBtn.setBounds(110, 340, 100, 20);
        bfsBtn.setBounds(220, 340, 120, 20);

        ButtonGroup group = new ButtonGroup();
        group.add(aStarBtn);
        group.add(bfsBtn);

        add(aStarBtn);
        add(bfsBtn);

        // --- Move Buttons ---
        int yStart = 380;
        JButton b1 = new JButton("1");
        JButton b2 = new JButton("2");
        JButton b3 = new JButton("3");
        JButton b4 = new JButton("4");

        b1.setBounds(40, yStart, 80, 40);
        b2.setBounds(125, yStart, 80, 40);
        b3.setBounds(210, yStart, 80, 40);
        b4.setBounds(295, yStart, 80, 40);

        add(b1); add(b2); add(b3); add(b4);

        // --- Action Buttons ---
        JButton compBtn = new JButton("Computer Move");
        JButton randBtn = new JButton("Randomize");

        compBtn.setBounds(40, yStart + 50, 335, 40);
        randBtn.setBounds(40, yStart + 100, 335, 40);

        add(compBtn);
        add(randBtn);

        // --- Event Listeners ---
        b1.addActionListener(e -> playerMove(1));
        b2.addActionListener(e -> playerMove(2));
        b3.addActionListener(e -> playerMove(3));
        b4.addActionListener(e -> playerMove(4));

        randBtn.addActionListener(e -> {
            board.randomize();
            refreshBoard();
        });

        compBtn.addActionListener(e -> {
            // 1. Check which radio button is selected
            boolean useBFS = bfsBtn.isSelected();
            
            // 2. Tell the computer which brain to use
            if (computer instanceof ComputerPlayer) {
                ((ComputerPlayer) computer).setAlgorithm(useBFS);
            }

            // 3. Get and execute the move
            int mv = computer.getMove();
            System.out.println("Computer chose Move " + mv + " using " + (useBFS ? "BFS" : "A*"));
            
            highlightRotation(mv);
            board.executeMove(mv);
            refreshBoard();
            checkSolved();
        });
    }

    private void playerMove(int move) {
        highlightRotation(move);
        board.executeMove(move);
        refreshBoard();
        checkSolved();
    }

    private void highlightRotation(int mvnum) {
        int[][] M = switch (mvnum) {
            case 1 -> new int[][]{{0,0},{1,0},{1,1},{0,1}};
            case 2 -> new int[][]{{1,0},{2,0},{2,1},{1,1}};
            case 3 -> new int[][]{{1,1},{2,1},{2,2},{1,2}};
            default -> new int[][]{{0,1},{1,1},{1,2},{0,2}};
        };

        for (int[] c : M) 
            cells[c[0]][c[1]].setBorder(BorderFactory.createLineBorder(Color.RED, 3));

        new Timer(250, e -> {
            for (int[] c : M) 
                cells[c[0]][c[1]].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }).start();
    }

    private void refreshBoard() {
        int[][] g = board.getGrid();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                cells[i][j].setText(String.valueOf(g[i][j]));
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
