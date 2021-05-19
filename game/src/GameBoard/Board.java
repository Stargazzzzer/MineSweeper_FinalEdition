package game.src.GameBoard;

import java.util.ArrayList;
import java.util.Random;

import game.src.listener.BoardListener;
import game.src.listener.Listenable;
//import game.src.player.Player;

public class Board implements Listenable<BoardListener> {
    public Square[][] board;
    private int row;
    private int column;
    private int squareCount;
    private int mineLeft;

    public static boolean isLastMoveValid;

    private boolean firstClicked;
    private boolean isError;

    private boolean isProgressing = false;

    private final ArrayList<BoardListener> listenerList = new ArrayList<>();

    public Board(int row, int column, int mineNum) {
        init(row, column, mineNum);
        isLastMoveValid = false;
    }

    public void init(int row, int column, int mineNum) {
        this.row = row;
        this.column = column;
        this.isProgressing = true;
        this.firstClicked = false;
        this.isError = false;
        squareCount = row * column;
        mineLeft = mineNum;
        board = new Square[row][column];

        // new Board
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = new Square(i, j);
            }
        }

        int temp1, temp2;
        int temp = 0;
        while (temp != mineNum) {
            Random random = new Random();
            double surroundingNum = 0.0D;
            temp1 = random.nextInt(row);
            temp2 = random.nextInt(column);

            for (int i = -1; i < 2; ++i) {
                for (int k = -1; k < 2; ++k) {
                    if (i + temp1 >= 0 && i + temp1 < this.row && k + temp2 >= 0 && k + temp2 < this.column) {
                        if (this.board[i + temp1][k + temp2].hasMine()) {
                            ++surroundingNum;
                        }
                    } else {
                        surroundingNum += 0.5D;
                    }
                }
            }

            double possibility = surroundingNum * 0.125D;
            if (random.nextDouble() > possibility && !this.board[temp1][temp2].hasMine()) {
                this.board[temp1][temp2].layMine();
                ++temp;
            }
        }

        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                if (this.board[i][j].getStatus() == -1) {
                    for (int k = -1; k <= 1; ++k) {
                        for (int l = -1; l <= 1; ++l) {
                            if (this.checkBorder(i + k, j + l) && this.board[i + k][j + l].getStatus() != -1) {
                                this.board[i + k][j + l].setStatus(this.board[i + k][j + l].getStatus() + 1);
                            }
                        }
                    }
                }
            }
        }
        
        listenerList.forEach(BoardListener::onBoardReload);
    }

    public boolean checkBorder(int x, int y) {
        return x >= 0 && x < this.row && y >= 0 && y < this.column;
    }

    // true for valid, false for loseScore or invalid
    public boolean clickSquare(Square square) {
        isLastMoveValid = true;

        if (!isProgressing || !square.isAccessible()) {
            isLastMoveValid = false;
            return false;
        }
        square.click();
        squareCount--;
        listenerList.forEach(listener -> listener.onSquareClicked(square));
        if (square.hasMine()) {
            if (!firstClicked) {
                isError = true;
            }
            mineLeft--;
            return false;
        }
        firstClicked = true;
        if (square.isClear()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (checkBorder(square.getX() + j, square.getY() + i)) {
                        clickSquare(square.getX() + j, square.getY() + i);
                        isLastMoveValid = true;
                    }
                }
            }
            return true;
        }
        return true;
    }

    public boolean clickSquare(int x, int y) {
        return clickSquare(getSquare(x, y));
    }

    // true for gainscore, false for make error

    public boolean rightClick(int x, int y) {
        isLastMoveValid = true;
        Square square = getSquare(x, y);
        if (!isProgressing || !square.isAccessible()) {
            isLastMoveValid = false;
            return false;
        }
        if (square.hasMine()) {
            square.flag();
            mineLeft--;
            listenerList.forEach(listener -> listener.onSquareClicked(square));
            return true;
        } else {
            clickSquare(square);
            return false;
        }
    }

    // true for valid, false for invalid
    public boolean middleClick(int x, int y) {
        isLastMoveValid = true;
        Square square = getSquare(x, y);
        if (!isProgressing || !square.isClicked() || square.isFlagged()) {
            isLastMoveValid = false;
            return false;
        }
        int count = 0;
        boolean fdb = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && checkBorder(x + i, y + j)) {
                    Square temp = getSquare(x + i, y + j);
                    if (temp.isFlagged() || (temp.hasMine() && temp.isClicked())) {
                        count++;
                    }
                }
            }
        }
        if (count == square.getStatus()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (checkBorder(x + i, y + j)) {
                        clickSquare(x + i, y + j);
                        if (isLastMoveValid)
                            fdb = true;
                    }
                }
            }
        }
        return fdb;
    }

    public void touchSquare(int x, int y) {
        listenerList.forEach(listener -> listener.onSquareTouched(getSquare(x, y)));
    }

    public Square getSquare(int x, int y) {
        return board[x][y];
    }

    public int getSquareCount() {
        return squareCount;
    }

    public boolean isProgressing() {
        return isProgressing;
    }

    public boolean isError() {
        return isError;
    }

    public int getMineLeft() {
        return this.mineLeft;
    }

    public void squareCountMinus() {
        squareCount--;
    }

    @Override
    public void addListener(BoardListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void cancelListener(BoardListener listener) {
        listenerList.remove(listener);
    }

    public ArrayList<BoardListener> getListenerList() {
        return this.listenerList;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setMineLeft(int mineLeft) {
        this.mineLeft = mineLeft;
    }
}
