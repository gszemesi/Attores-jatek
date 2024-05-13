
import java.awt.Point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *  Board osztály. 
 * @author Szemesi Gábor
 */
public class Board {

    private Field[][] board;
    private final int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        board = new Field[this.boardSize][this.boardSize];
        for (int i = 0; i < this.boardSize; ++i) {
            for (int j = 0; j < this.boardSize; ++j) {
                board[i][j] = new Field();
            }
        }
    }
    /**
     * Visszadja, hogy vége van-e a játéknak és ha igen, melyik játékos nyert.
     * @return 0 ha nincs vége a játéknak. 1 ha az egyes játékos nyert. 2 ha az második játékos nyert.
     */
    public int isOver() {
        for (int i = 0; i < boardSize; i++) {
            if (board[0][i].getPlayer_id()==2) {
                return 1;
            }
        }
        for (int i = 0; i < boardSize; i++) {
            if (board[boardSize-1][i].getPlayer_id()==1) {
                return 2;
            }
        }
        return 0;
    }

    public Field get(int x, int y) {
        return board[x][y];
    }

    public Field get(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        return get(x, y);
    }

    public int getBoardSize() {
        return boardSize;
    }
}
