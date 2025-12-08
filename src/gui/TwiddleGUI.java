package gui;

import game.*;

import javax.swing.*;
import java.awt.*;

public class TwiddleGUI extends JFrame {

    private Board board;
    private JLabel[][] cells = new JLabel[3][3];
    private GameEngine engine;
    private Player human;
    private Player computer;

    public TwiddleGUI() {

        board = new Board();
        human = new HumanPlayer(board);
        computer = new ComputerPlayer(board);
        engine = new GameEngine(board, human, computer);

        setTitle("Twiddle Puzzle");
        setSize(400, 520);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupGrid();
        setupButtons();
        refreshBoard();

        setVisible(true);
    }


    private void setupGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        gridPanel.setBounds(50, 30, 300, 300);

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


    private void setupButtons() {

        JButton b1 = new JButton("Move 1");
        JButton b2 = new JButton("Move 2");
        JButton b3 = new JButton("Move 3");
        JButton b4 = new JButton("Move 4");
        JButton compBtn = new JButton("Computer Move");
        JButton randBtn = new JButton("Randomize");

        b1.setBounds(40, 350, 100, 40);
        b2.setBounds(150, 350, 100, 40);
        b3.setBounds(260, 350, 100, 40);
        b4.setBounds(40, 400, 100, 40);
        compBtn.setBounds(150, 400, 210, 40);
        randBtn.setBounds(40, 450, 320, 40);

        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(compBtn);
        add(randBtn);

        b1.addActionListener(e -> playerMove(1));
        b2.addActionListener(e -> playerMove(2));
        b3.addActionListener(e -> playerMove(3));
        b4.addActionListener(e -> playerMove(4));

        compBtn.addActionListener(e -> {
            int mv = computer.getMove();
            highlightRotation(mv);   
            board.executeMove(mv);
            refreshBoard();
            checkSolved();
        });

        randBtn.addActionListener(e -> {
            board.randomize();
            refreshBoard();
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

        // ✅ Change border color to RED
        for (int[] c : M) {
            cells[c[0]][c[1]]
                .setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        }

        // ✅ Timer to revert back to BLACK after 250ms
        new Timer(250, e -> {
            for (int[] c : M) {
                cells[c[0]][c[1]]
                    .setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
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
