package game.src.display;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import game.src.utils.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.src.GameBoard.Board;
import game.src.GameBoard.Square;
import game.src.listener.*;
import game.src.player.Player;

public class BoardComponent extends JComponent implements BoardListener, GameStatusListener, Listenable<InputListener> {
    private int row;
    private int column;
    private int squareSize;
    private boolean isRevealed = false;

    private final ArrayList<InputListener> listenerList = new ArrayList<>();

    public BoardComponent(int row, int column, int squareSize) {
        SquareBtn.initIcon(squareSize);
        this.row = row;
        this.column = column;
        this.squareSize = squareSize;
        setLayout(new GridLayout(row, column, squareSize / 15, squareSize / 15));
        setBounds(5, 60, column * squareSize, row * squareSize);
        addBtns();
        setVisible(true);
    }

    public void addBtns() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                SquareBtn button = new SquareBtn(squareSize);
                int finalI = i;
                int finalJ = j;
                button.addMouseListener(new MouseAdapter() {
                    boolean isDown = false;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        isDown = true;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (isDown) {
                            isDown = false;
                            ClickType type = ClickType.NULL;
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                type = ClickType.LEFT_CLICK;
                                new ButtonPlayer("game/src/view/sounds/ButtonSound1.wav");
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                type = ClickType.RIGHT_CLICK;
                                new ButtonPlayer("game/src/view/sounds/ButtonSound1.wav");
                            } else if (e.getButton() == MouseEvent.BUTTON2) {
                                type = ClickType.MIDDLE_CLICK;
                                new ButtonPlayer("game/src/view/sounds/ButtonSound1.wav");
                            }
                            ClickType finalType = type;
                            listenerList.forEach(listener -> listener.onBoardClicked(finalI, finalJ, finalType));
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!isDown)
                            listenerList.forEach(listener -> listener.onMouseUp(finalI, finalJ));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!isDown)
                            listenerList.forEach(listener -> listener.onMouseDown(finalI, finalJ));
                    }

                });
                this.add(button);
            }
        }
    }

    @Override
    public void onGameStatusChanged(GameStatus status, Player[] players, boolean isSinglePlayer) {
        if (isSinglePlayer) {
            switch (status) {
                case END:
                    RankingFrame.getInstance(players).setVisible(true);
                    break;
                case PROGRESSING:
                    return;
                case Error:
                    onBoardReload();
                    return;
                case LOSE:
                    JOptionPane.showMessageDialog(null, "You clicked a mine!", "GameOver", JOptionPane.PLAIN_MESSAGE);
                    break;
                case MISSCLICK:
                    JOptionPane.showMessageDialog(null, "You right clicked a wrong block!", "GameOver",
                            JOptionPane.PLAIN_MESSAGE);
                    break;
            }

        } else {
            switch (status) {
                case END:
                    RankingFrame.getInstance(players).setVisible(true);
                    break;
                case PROGRESSING:
                    return;
                case Error:
                    onBoardReload();
                    return;
                default:
                    return;
            }

        }
    }

    @Override
    public void onBoardReload() {
        for (int i = 0; i < row * column; i++) {
            JButton button = (JButton) getComponent(i);
            button.setIcon(SquareBtn.unclicked);
            button.setEnabled(true);
        }
        repaint();
    }

    @Override
    public void onSquareClicked(Square square) {
        JButton button = (JButton) getComponent(square.getY() + square.getX() * column);
        if (square.isClicked()) {
            button.setEnabled(false);
            repaint();
            changeIcon(square);
            return;
        }
        if (square.isFlagged())
            button.setIcon(SquareBtn.flagicon);
    }

    @Override
    public void onBoardRevealed(Board board) {
        this.isRevealed = !isRevealed;
        if (isRevealed) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    JButton button = (JButton) getComponent(j + i * column);
                    if (button.isEnabled() && !button.getIcon().equals(SquareBtn.flagicon)
                            && board.getSquare(i, j).getStatus() == -1) {
                        button.setIcon(SquareBtn.unclickedMine);
                    }
                }
            }

        } else {
            for (int i = 0; i < row * column; i++) {
                JButton button = (JButton) getComponent(i);
                if (button.isEnabled() && !button.getIcon().equals(SquareBtn.flagicon)) {
                    button.setIcon(SquareBtn.unclicked);
                }
            }
        }
    }

    @Override
    public void onBoardRecovered(Board board) {

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                JButton button = (JButton) getComponent(j + i * column);
                button.setVisible(true);
                button.setIcon(SquareBtn.unclicked);
                button.setEnabled(true);
                repaint();
                board.board[i][j].recover();
            }
        }
    }

    @Override
    public void onSquareTouched(Square square) {
        changeIcon(square);
    }

    @Override
    public void addListener(InputListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void cancelListener(InputListener listener) {
        listenerList.remove(listener);
    }

    public void changeIcon(Square square) {
        JButton button = (JButton) getComponent(square.getY() + square.getX() * column);
        if (square.isClicked()) {
            switch (square.getStatus()) {
                case -1:
                    button.setDisabledIcon(SquareBtn.number_1);
                    break;
                case 0:
                    button.setDisabledIcon(SquareBtn.number0);
                    break;
                case 1:
                    button.setDisabledIcon(SquareBtn.number1);
                    break;
                case 2:
                    button.setDisabledIcon(SquareBtn.number2);
                    break;
                case 3:
                    button.setDisabledIcon(SquareBtn.number3);
                    break;
                case 4:
                    button.setDisabledIcon(SquareBtn.number4);
                    break;
                case 5:
                    button.setDisabledIcon(SquareBtn.number5);
                    break;
                case 6:
                    button.setDisabledIcon(SquareBtn.number6);
                    break;
                case 7:
                    button.setDisabledIcon(SquareBtn.number7);
                    break;
                case 8:
                    button.setDisabledIcon(SquareBtn.number8);
                    break;
                default:
                    break;
            }
        } else {
            if (!isRevealed && !square.isFlagged()) {
                if (square.isMouseUp)
                    button.setIcon(SquareBtn.unclickedLight);
                else
                    button.setIcon(SquareBtn.unclicked);
            }
        }
    }
}
