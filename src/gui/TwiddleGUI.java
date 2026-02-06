package gui;

import game.*;
import java.awt.*;
import javax.swing.*;

public class TwiddleGUI extends JFrame {

    private Board board;
    private JLabel[][] cells;
    private Player computer;

    private JComboBox<String> algoBox;
    private JLabel moveLabel;

    public TwiddleGUI(int N) {
        board = new Board(N);
        computer = new ComputerPlayer(board);

        setTitle("Twiddle Puzzle");
        setSize(520, 650);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupGrid(N);
        setupControls(N);
        refreshBoard();
        updateMoves();

        setVisible(true);
    }

    private void setupGrid(int N) {
        cells = new JLabel[N][N];
        JPanel gridPanel = new JPanel(new GridLayout(N, N));
        gridPanel.setBounds(40, 30, 440, 440);

        Font f = new Font("Arial", Font.BOLD, 22);

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                JLabel l = new JLabel("", SwingConstants.CENTER);
                l.setFont(f);
                l.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                cells[i][j] = l;
                gridPanel.add(l);
            }
        add(gridPanel);
    }

    private void setupControls(int N) {
        algoBox = new JComboBox<>(new String[]{
                "A*", "BFS", "Divide & Conquer"
        });
        algoBox.setBounds(40, 490, 200, 30);
        add(algoBox);

        moveLabel = new JLabel("Moves: 0");
        moveLabel.setBounds(260, 490, 150, 30);
        add(moveLabel);

        JPanel buttons = new JPanel(new GridLayout(0, N - 1));
        buttons.setBounds(40, 530, 440, 60);

        for (int i = 1; i <= board.totalMoves(); i++) {
            final int mv = i;
            JButton b = new JButton("" + i);
            b.addActionListener(e -> doMove(mv));
            buttons.add(b);
        }
        add(buttons);

        JButton comp = new JButton("Computer Move");
        comp.setBounds(40, 600, 440, 30);
        comp.addActionListener(e -> computerMove());
        add(comp);
    }

    private void doMove(int mv) {
        board.executeMove(mv);
        refreshBoard();
        updateMoves();
        checkSolved();
    }

    private void computerMove() {
        String choice = (String) algoBox.getSelectedItem();

        if (choice.equals("Divide & Conquer"))
            computer = new ComputerPlayer2(board);
        else {
            ComputerPlayer cp = new ComputerPlayer(board);
            cp.setAlgorithm(choice.equals("BFS"));
            computer = cp;
        }

        doMove(computer.getMove());
    }

    private void refreshBoard() {
        int[][] g = board.getGrid();
        for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[0].length; j++)
                cells[i][j].setText("" + g[i][j]);
    }

    private void updateMoves() {
        moveLabel.setText("Moves: " + board.getMoves());
    }

    private void checkSolved() {
        if (board.isSolved())
            JOptionPane.showMessageDialog(this, "🎉 Puzzle Solved!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TwiddleGUI(4)); // change N here
    }
}
