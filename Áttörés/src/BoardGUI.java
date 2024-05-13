

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Játék létrehozása és játéktér frissitése. Létre hozza gombokat, beállítja a
 * Labelt.
 *
 * @author Szemesi Gábor
 */
public class BoardGUI {

    private Board board;
    private JLabel Label;
    private JPanel boardPanel;
    private JButton[][] buttons;
    private ArrayList<Point> selected;

    private final Color myColor = Color.yellow;
    private final Color stepColor = Color.green;
    private final Color player1color = Color.white;
    private final Color player2color = Color.black;
    private Color turnColor;
    private final Color bg = new Color(100, 100, 100);

    private int turnCounter;

    private final ImageIcon iconWhite;
    private final ImageIcon iconBlack;

    /**
     * Játéktér megjelenítése. Babúk iconjának beállítása. Gombok létrehozása.
     * Bábúk felrakása a táblára.
     *
     */
    public BoardGUI(int boardSize) {
        iconWhite = new ImageIcon("icons/white.png");
        iconBlack = new ImageIcon("icons/black.png");
        turnCounter = 1;
        turnColor = player1color;
        Label = new JLabel("turn: " + turnCounter + " | " + (turnColor == player1color ? "white" : "black") + "'s turn");
        Label.setHorizontalAlignment(JLabel.CENTER);

        board = new Board(boardSize);
        boardPanel = new JPanel();
        selected = new ArrayList<>();

        boardPanel.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
        buttons = new JButton[board.getBoardSize()][board.getBoardSize()];

        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                JButton button = new JButton();
                button.addActionListener(new ButtonListener(i, j));
                button.setPreferredSize(new Dimension(50, 50));
                buttons[i][j] = button;
                boardPanel.add(button);
                button.setBackground(bg);
            }
        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            board.get(new Point(0, i)).setPlayer_id(1);
            board.get(new Point(1, i)).setPlayer_id(1);
            board.get(new Point(board.getBoardSize() - 2, i)).setPlayer_id(2);
            board.get(new Point(board.getBoardSize() - 1, i)).setPlayer_id(2);
        }
        refresh();
    }

    /**
     * Megadja melyik játékos lép és hányadik kör van.
     */
    private void nextTurn() {
        if (turnColor == player1color) {
            turnColor = player2color;
        } else {
            turnColor = player1color;
            turnCounter++;
        }
        refresh();
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    public JLabel getLabel() {
        return Label;
    }

    /**
     * Játéktér frissitése. Labelben megjelenití hányadik kör van és melyik
     * játékos lép. Board újra alkotása. Ha játéknak vége üzenet jelenik meg,
     * melyik játékos nyert és új játék kezdödik.
     */
    public void refresh() {
        Label.setText(" turn: " + turnCounter + " | " + (turnColor == player1color ? "white" : "black") + "'s turn");
        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                Field field = board.get(i, j);
                JButton button = buttons[i][j];
                if (selected.size() == 0) {
                    button.setBackground(bg);
                } else {
                    Point thisPoint = new Point(i, j);
                    for (Point p : selected) {
                        if (p == thisPoint) {
                            button.setBackground(stepColor);
                        }

                    }
                }
                button.setIcon(null);
                if (field.getPlayer_id() == 1) {
                    button.setIcon(iconWhite);
                }
                if (field.getPlayer_id() == 2) {
                    button.setIcon(iconBlack);
                }
            }
        }
        if (board.isOver() == 1) {
            resetBoard(board.getBoardSize());
            JOptionPane.showMessageDialog(boardPanel, "Black player win!", "Congrats!", JOptionPane.PLAIN_MESSAGE);
        }
        if (board.isOver() == 2) {
            resetBoard(board.getBoardSize());
            JOptionPane.showMessageDialog(boardPanel, "White player win!", "Congrats!", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Vissza állítja a táblát alaphelyzetbe.
     *
     * @param boardSize Tábla mérete
     */
    public void resetBoard(int boardSize) {
        boardPanel.removeAll();

        turnCounter = 1;
        turnColor = player1color;
        Label.setText(" turn: " + turnCounter + " | " + (turnColor == player1color ? "white" : "black") + "'s turn");

        board = new Board(boardSize);
        selected = new ArrayList<>();

        boardPanel.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
        buttons = new JButton[board.getBoardSize()][board.getBoardSize()];

        for (int i = 0; i < board.getBoardSize(); ++i) {
            for (int j = 0; j < board.getBoardSize(); ++j) {
                JButton button = new JButton();
                button.addActionListener(new ButtonListener(i, j));
                button.setPreferredSize(new Dimension(50, 50));
                buttons[i][j] = button;
                boardPanel.add(button);
                board.get(new Point(i, j)).setPlayer_id(0);
            }
        }

        for (int i = 0; i < board.getBoardSize(); i++) {
            board.get(new Point(0, i)).setPlayer_id(1);
            board.get(new Point(1, i)).setPlayer_id(1);
            board.get(new Point(board.getBoardSize() - 2, i)).setPlayer_id(2);
            board.get(new Point(board.getBoardSize() - 1, i)).setPlayer_id(2);
        }
        refresh();
    }

    /**
     * Gomb kattintás vizsgálata. A játéktábla nyomógombjaihoz közös
     * eseménykezelőt rendelünk. Egérkattintás hatására kijelöli a kattintott
     * cellát, vagy ha már ki van jelölve egy cella, akkor a kijelölt cellára
     * kattintva visszavonhatjuk a kijelölést. Egy megjelölt cellára kattintva
     * pedig átrakhatjuk a kijelölt cellában lévő bábút.
     */
    class ButtonListener implements ActionListener {

        private int x, y;

        public ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Point thisPoint = new Point(x, y);
            if (selected.size() != 0 && selected.get(0).equals(thisPoint)) {
                selected.clear();
                refresh();
            } else if (selected.size() == 0 && board.get(x, y).getPlayer_id() == 1 && turnColor.equals(player1color)) {
                selected.add(new Point(x, y));
                buttons[x][y].setBackground(myColor);
                if (x + 1 < board.getBoardSize() && y - 1 >= 0 && board.get(x + 1, y - 1).getPlayer_id() != 1) {
                    selected.add(new Point(x + 1, y - 1));
                    buttons[x + 1][y - 1].setBackground(stepColor);
                }
                if (x + 1 >= 0 && y + 1 < board.getBoardSize() && board.get(x + 1, y + 1).getPlayer_id() != 1) {
                    selected.add(new Point(x + 1, y + 1));
                    buttons[x + 1][y + 1].setBackground(stepColor);
                }
                if (board.get(x + 1, y).getPlayer_id() == 0 && board.get(x + 1, y).getPlayer_id() != 1) {
                    selected.add(new Point(x + 1, y));
                    buttons[x + 1][y].setBackground(stepColor);
                }
                refresh();
            } else if (selected.size() == 0 && board.get(x, y).getPlayer_id() == 2 && turnColor.equals(player2color)) {
                selected.add(new Point(x, y));
                buttons[x][y].setBackground(myColor);
                if (x - 1 >= 0 && y - 1 >= 0 && board.get(x - 1, y - 1).getPlayer_id() != 2) {
                    selected.add(new Point(x - 1, y - 1));
                    buttons[x - 1][y - 1].setBackground(stepColor);
                }
                if (x - 1 <= board.getBoardSize() && y + 1 < board.getBoardSize() && board.get(x - 1, y + 1).getPlayer_id() != 2) {
                    selected.add(new Point(x - 1, y + 1));
                    buttons[x - 1][y + 1].setBackground(stepColor);
                }
                if (board.get(x - 1, y).getPlayer_id() == 0 && board.get(x - 1, y).getPlayer_id() != 2) {
                    selected.add(new Point(x - 1, y));
                    buttons[x - 1][y].setBackground(stepColor);
                }
                refresh();
            } else if (turnColor.equals(player1color) && buttons[x][y].getBackground().equals(stepColor)) {
                board.get(x, y).setPlayer_id(1);
                board.get(selected.get(0)).setPlayer_id(0);
                selected.clear();
                nextTurn();
            } else if (turnColor.equals(player2color) && buttons[x][y].getBackground().equals(stepColor)) {
                board.get(x, y).setPlayer_id(2);
                board.get(selected.get(0)).setPlayer_id(0);
                selected.clear();
                nextTurn();
            }
        }

    }

}
