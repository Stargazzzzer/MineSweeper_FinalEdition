package game.src.GameBoard;

public class Square {
    private int x;
    private int y;
    public int status;
    private boolean isClicked = false;
    private boolean isFlagged = false;
    public double hasMinePossibility = 0d;
    public double noMinePossibility = 0d;
    public boolean isMouseUp = false;

    // x,y represent the location of the square
    public Square() {
    }

    public void recover() {
        this.isFlagged = false;
        this.isClicked = false;
    }

    public void setPossibility() {
        this.noMinePossibility = 1f - hasMinePossibility;
    }

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasMine() {
        return status == -1;
    }

    public void layMine() {
        this.status = -1;
    }

    public boolean isClear() {
        return status == 0;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }

    public void click() {
        this.isClicked = true;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void flag() {
        isFlagged = true;
    }

    public boolean isAccessible() {
        return !isClicked && !isFlagged;
    }
}
