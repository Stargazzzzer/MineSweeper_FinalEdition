package game.src.player;

import java.util.List;
import game.src.GameBoard.*;
import game.src.utils.ClickType;
import game.src.utils.PlayerType;

import java.util.Random;

import java.util.ArrayList;

public class AIOperator {
    private final List<Square> unClickedSquares = new ArrayList<>();
    double IQ;
    int errorPossibility;// from 0-10,(1->2->4)
    private ClickType AIClick = ClickType.LEFT_CLICK;
    private Square MineSpotted = new Square();
    boolean spottedMine = false;
    boolean firstClick = false;
    boolean isCrazy = false;

    public void setClickOrder(Board gameBoard) {
        for (int row = 0; row < gameBoard.getRow(); row++) {
            for (int column = 0; column < gameBoard.getColumn(); column++) {
                Square square = gameBoard.getSquare(row, column);
                if (square.isClicked() && square.status > 0) {
                    int surroundingMines = 0;
                    int surroundingBlanks = 0;
                    int surroundingSquares = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int k = -1; k <= 1; k++) {
                            if ((i != 0 || k != 0) && i + row >= 0 && i + row < gameBoard.getRow() && k + column >= 0
                                    && k + column < gameBoard.getColumn()) {
                                Square currentSquare = gameBoard.getSquare(row + i, column + k);
                                surroundingSquares++;
                                if (currentSquare.isClicked() && currentSquare.status == 0) {
                                    surroundingBlanks++;
                                    continue;
                                }
                                if (currentSquare.isClear()) {
                                    surroundingBlanks++;
                                    continue;
                                }
                                if (currentSquare.isClicked() && currentSquare.hasMine()) {
                                    surroundingMines++;
                                    continue;
                                }
                                if (currentSquare.isClicked() && currentSquare.status == -1) {
                                    surroundingMines++;
                                    continue;
                                }
                                if (currentSquare.isFlagged()) {
                                    surroundingMines++;
                                }
                            }
                        }
                    }
                    double hasMinePossibility = ((double) (square.status - surroundingMines)
                            / (double) (surroundingSquares - surroundingMines - surroundingBlanks));
                    for (int i = -1; i <= 1; i++) {
                        for (int k = -1; k <= 1; k++) {
                            if ((i + row >= 0 && i + row < gameBoard.getRow())
                                    && (k + column >= 0 && k + column < gameBoard.getColumn())) {
                                if (gameBoard.board[i + row][k + column].isAccessible()) {
                                    if ((surroundingSquares - surroundingBlanks) == surroundingMines) {
                                        spottedMine = true;
                                        MineSpotted = gameBoard.board[i + row][k + column];
                                    }
                                    gameBoard.board[i + row][k + column].hasMinePossibility += hasMinePossibility;
                                    gameBoard.board[i + row][k + column].setPossibility();
                                }
                            }
                        }
                    }
                }
            }
        }
        // sort unclicked squares to get clickorder:

    }

    public Square sortBlankOrder() {
        if (unClickedSquares.size() == 0) {
            return null;
        }
        Square max = unClickedSquares.get(0);
        for (int i = 0; i < unClickedSquares.size(); i++) {
            if (Double.doubleToLongBits(unClickedSquares.get(i).noMinePossibility) >= Double
                    .doubleToLongBits(max.noMinePossibility)) {
                if (this.isCrazy) {
                    if (!unClickedSquares.get(i).hasMine()) {
                        max = unClickedSquares.get(i);
                    } else {
                        continue;
                    }
                } else {
                    if (unClickedSquares.get(i).noMinePossibility <= 0.1d) {
                        max = unClickedSquares.get(i);
                    } else {
                        max = unClickedSquares.get(i);
                    }

                }

            }

        }
        return max;
    }

    public Square sortMineOrder() {
        if (unClickedSquares.size() == 0) {
            return null;
        }
        Square max = unClickedSquares.get(0);
        for (int i = 0; i < unClickedSquares.size(); i++) {
            if (Double.doubleToLongBits(unClickedSquares.get(i).hasMinePossibility) >= Double
                    .doubleToLongBits(max.hasMinePossibility)) {
                if (this.isCrazy) {
                    if (unClickedSquares.get(i).hasMine()) {
                        max = unClickedSquares.get(i);

                    } else {
                        continue;
                    }
                } else {
                    max = unClickedSquares.get(i);
                }
            }
        }
        return max;
    }

    public void setIQ(PlayerType type) {
        if (type == PlayerType.AI_ROOKIE) {
            this.IQ = 1.0d;
            this.errorPossibility = 4;
            this.firstClick = true;
        }
        if (type == PlayerType.AI_NORMAL) {
            this.IQ = 0.75d;
            this.errorPossibility = 1;
            this.firstClick = true;
        }
        if (type == PlayerType.AI_CRAZY) {
            this.IQ = 0.75d;
            this.errorPossibility = 0;
            this.firstClick = true;
            this.isCrazy = true;
        }

    }

    public void setUnClickedSquares(Board gameBoard) {
        for (int r = 0; r < gameBoard.getRow(); r++) {
            for (int k = 0; k < gameBoard.getColumn(); k++) {
                if (gameBoard.getSquare(r, k).isAccessible()) {
                    gameBoard.getSquare(r, k).noMinePossibility = 0.0d;
                    unClickedSquares.add(gameBoard.getSquare(r, k));
                }
            }
        }
    }

    public ClickType getAIClick() {
        return this.AIClick;
    }

    public void initPossibility(Board gameBoard) {
        for (int i = 0; i < gameBoard.getRow(); i++) {
            for (int k = 0; k < gameBoard.getColumn(); k++) {
                gameBoard.board[i][k].hasMinePossibility = 0;
                gameBoard.board[i][k].noMinePossibility = 0;
            }
        }
    }

    public Square AI_Movement(Board gameBoard, PlayerType type) {
        initPossibility(gameBoard);
        setUnClickedSquares(gameBoard);
        setClickOrder(gameBoard);
        setIQ(type);
        Random random = new Random();
        if (spottedMine) {
            this.AIClick = ClickType.RIGHT_CLICK;
            return MineSpotted;
        }
        if (unClickedSquares.size() == (gameBoard.getRow()) * gameBoard.getColumn()) {
            return gameBoard.getSquare(random.nextInt(gameBoard.getRow()), random.nextInt(gameBoard.getColumn()));
        }
        if (sortBlankOrder() == null && sortMineOrder() == null) {
            return null;
        }
        if (sortBlankOrder().noMinePossibility == 0 && sortMineOrder().hasMinePossibility == 0) {
            this.AIClick = ClickType.LEFT_CLICK;
            return unClickedSquares.get(random.nextInt(unClickedSquares.size()));
        }
        // ai make an random click:

        if (errorPossibility > random.nextInt(10)) {
            // random bound cannot be zero
            if (random.nextInt(10) >= 7) {
                this.AIClick = ClickType.RIGHT_CLICK;
            } else {
                this.AIClick = ClickType.LEFT_CLICK;
            }
            return unClickedSquares.get(random.nextInt(unClickedSquares.size()));
        }
        // normal click:
        Square blankPossibleMost = sortBlankOrder();
        Square minePossibleMost = sortMineOrder();
        if (Double.doubleToLongBits(minePossibleMost.hasMinePossibility) >= Double.doubleToLongBits(IQ)) {
            this.AIClick = ClickType.RIGHT_CLICK;
            return minePossibleMost;
        }
        if (Double.doubleToLongBits(minePossibleMost.hasMinePossibility) >= Double
                .doubleToLongBits(sortBlankOrder().noMinePossibility)) {
            this.AIClick = ClickType.RIGHT_CLICK;
            return minePossibleMost;
        }
        this.AIClick = ClickType.LEFT_CLICK;
        return blankPossibleMost;
    }
}
