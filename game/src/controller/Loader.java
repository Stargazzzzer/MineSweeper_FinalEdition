package game.src.controller;

import java.io.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game.src.GameBoard.Board;
import game.src.GameBoard.Square;
import game.src.display.ButtonPlayer;
import game.src.display.GameFrame;
import game.src.listener.GameStatusListener;
import game.src.listener.InputListener;
import game.src.listener.Listenable;
import game.src.player.Player;
import game.src.utils.ClickType;
import game.src.utils.GameStatus;
import game.src.utils.PlayerType;

public class Loader implements InputListener, Listenable<GameStatusListener> {
    private GameFrame gameFrame;
    private Board gameBoard;
    private GameStatus gameStatus;
    private int index;
    private int counter = 0;
    private Player[] playerlist = new Player[4];
    private int row;
    private int column;
    private int mineNum;
    private boolean isSinglePlayer = false;

    private final ArrayList<GameStatusListener> listenerList = new ArrayList<>();

    public Loader(String filename) {
        File file = new File("game/src/controller/out/" + filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Error: The file doesn't exist!", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader("game/src/controller/out/" + filename));
            String temp = new String();
            temp = in.readLine();
            temp = in.readLine();
            String[] rec = temp.split(" ");
            if (!lenthCheck(rec, 5)) {
                in.close();
                JOptionPane.showMessageDialog(null, "Error in the file! Please check the file.", "Error",
                        JOptionPane.PLAIN_MESSAGE);
                return;
            }
            row = Integer.parseInt(rec[0]);
            column = Integer.parseInt(rec[1]);
            mineNum = Integer.parseInt(rec[2]);

            gameBoard = new Board(row, column, mineNum);

            playerlist = new Player[4];
            for (int i = 0; i < 4; i++) {
                temp = in.readLine();
                rec = temp.split(" ");
                switch (rec[0]) {
                    case "PLAYER":
                        playerlist[i] = new Player("Player" + Integer.toString(i + 1));
                        playerlist[i].setType(PlayerType.PLAYER);
                        playerlist[i].addListener(this);
                        break;
                    case "AI_ROOKIE":
                        playerlist[i] = new Player("AI_Rookie" + Integer.toString(i + 1));
                        playerlist[i].setType(PlayerType.AI_ROOKIE);
                        playerlist[i].addListener(this);
                        break;
                    case "AI_NORMAL":
                        playerlist[i] = new Player("AI_Normal" + Integer.toString(i + 1));
                        playerlist[i].setType(PlayerType.AI_NORMAL);
                        playerlist[i].addListener(this);
                        break;
                    case "AI_CRAZY":
                        playerlist[i] = new Player("AI_Crazy" + Integer.toString(i + 1));
                        playerlist[i].setType(PlayerType.AI_CRAZY);
                        playerlist[i].addListener(this);
                        break;
                    case "CLOSE":
                        playerlist[i] = new Player("Closed");
                        playerlist[i].setType(PlayerType.CLOSE);
                        break;
                    default:
                        break;
                }
                playerlist[i].setScore(Integer.parseInt(rec[1]));
                playerlist[i].setErr(Integer.parseInt(rec[2]));
                playerlist[i].setAllMoves(Integer.parseInt(rec[3]));
            }

            int cnt = 0;
            for (int i = 0; i < playerlist.length; i++) {
                if (playerlist[i].getType() != PlayerType.CLOSE)
                    cnt++;
            }
            if (cnt == 1)
                isSinglePlayer = true;

            gameFrame = new GameFrame(row, column, playerlist);
            gameBoard.addListener(gameFrame.getBoardComponent());
            gameFrame.addListener(this);
            gameFrame.getBoardComponent().addListener(this);
            gameFrame.getSaveBtn().addActionListener(event -> new Saver(this));
            addListener(gameFrame.getBoardComponent());

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    temp = in.readLine();
                    rec = temp.split(" ");
                    gameBoard.getSquare(i, j).setStatus(Integer.parseInt(rec[0]));
                    if (Integer.parseInt(rec[1]) == 1) {
                        if (gameBoard.getSquare(i, j).hasMine())
                            gameBoard.setMineLeft(gameBoard.getMineLeft() - 1);
                        final int finalI = i;
                        final int finalJ = j;
                        gameBoard.getSquare(finalI, finalJ).click();
                        gameBoard.squareCountMinus();
                        final Square tempSquare = gameBoard.getSquare(finalI, finalJ);
                        gameBoard.getListenerList().forEach(listner -> listner.onSquareClicked(tempSquare));
                    }
                    if (Integer.parseInt(rec[2]) == 1)
                        gameBoard.rightClick(i, j);
                }
            }
            temp = in.readLine();
            index = Integer.parseInt(temp);
            temp = in.readLine();
            counter = Integer.parseInt(temp);
            in.close();
            gameFrame.getScoreBoard().onPlayerChanged(index);

            this.gameStatus = GameStatus.PROGRESSING;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: Loading failed!", "Error", JOptionPane.PLAIN_MESSAGE);
        }

    }

    public boolean lenthCheck(String[] temp, int len) {
        return temp.length == len;
    }

    @Override
    public void onBoardClicked(int x, int y, ClickType type) {
        if (gameStatus != GameStatus.PROGRESSING)
            return;
        /**
         * Check the currentPlayer() if is Player, then go with the player's decision if
         * is AI, then auto click add the counter while do the valid move
         */

        // left
        if (type == ClickType.LEFT_CLICK) {
            boolean temp = gameBoard.clickSquare(x, y);
            while (gameBoard.isError()) {
                onGameRestart();
                temp = gameBoard.clickSquare(x, y);
            }
            if (Board.isLastMoveValid) {
                if (temp) {
                    gameFrame.getScoreBoard()
                            .showMove(currentPlayer().getName() + " clicks at (" + y + ", " + x + ").");
                } else {
                    gameFrame.getScoreBoard()
                            .showMove(currentPlayer().getName() + " clicks at (" + y + ", " + x + ").");
                    if (isSinglePlayer)
                        gameStatus = GameStatus.LOSE;
                    loseOnePoint();
                }
                counterPlus();
            }
        }

        // right
        if (type == ClickType.RIGHT_CLICK) {
            boolean temp = gameBoard.rightClick(x, y);
            if (Board.isLastMoveValid) {
                gameFrame.getScoreBoard()
                        .showMove(currentPlayer().getName() + " right clicks at (" + y + ", " + x + ").");
                if (temp) {
                    getOnePoint();
                } else {
                    if (isSinglePlayer)
                        gameStatus = GameStatus.MISSCLICK;
                    makeOneErr();
                }
                counterPlus();
            }
        }
        // middle
        if (type == ClickType.MIDDLE_CLICK) {
            boolean temp = gameBoard.middleClick(x, y);
            if (temp) {
                gameFrame.getScoreBoard()
                        .showMove(currentPlayer().getName() + " middle clicks at (" + y + ", " + x + ").");
                counterPlus();
            }
        }
        if (gameBoard.getMineLeft() == 0)
            gameStatus = GameStatus.END;
        listenerList.forEach(listener -> listener.onGameStatusChanged(gameStatus, playerlist, isSinglePlayer));

    }

    /**
     * same method for ai to click
     * 
     */
    @Override
    public void autoBoardClick(int x, int y, ClickType type) {
        if (gameStatus != GameStatus.PROGRESSING)
            return;
        int lastCounter = counter;
        // left
        if (x == 999 && y == 999 && type == ClickType.MIDDLE_CLICK) {
            listenerList.forEach(listener -> listener.onGameStatusChanged(GameStatus.END, playerlist, isSinglePlayer));
            return;
        }
        if (type == ClickType.LEFT_CLICK) {
            boolean temp = gameBoard.clickSquare(x, y);
            while (gameBoard.isError()) {
                onGameRestart();
                temp = gameBoard.clickSquare(x, y);
            }
            if (Board.isLastMoveValid) {
                gameBoard.board[x][y].click();
                if (temp) {
                    gameFrame.getScoreBoard()
                            .showMove(currentPlayer().getName() + " clicks at (" + y + ", " + x + ").");
                } else {
                    gameFrame.getScoreBoard()
                            .showMove(currentPlayer().getName() + " clicks at (" + y + ", " + x + ").");
                    if (isSinglePlayer)
                        gameStatus = GameStatus.LOSE;
                    loseOnePoint();
                }
                counterPlus();
            }
        }

        // right
        if (type == ClickType.RIGHT_CLICK) {
            boolean temp = gameBoard.rightClick(x, y);
            if (Board.isLastMoveValid) {
                gameBoard.board[x][y].click();
                gameFrame.getScoreBoard()
                        .showMove(currentPlayer().getName() + " right clicks at (" + y + ", " + x + ").");
                if (temp) {
                    getOnePoint();
                } else {
                    if (isSinglePlayer)
                        gameStatus = GameStatus.MISSCLICK;
                    makeOneErr();
                }
                counterPlus();
            }
        }
        // middle
        if (type == ClickType.MIDDLE_CLICK) {
            boolean temp = gameBoard.middleClick(x, y);
            if (temp) {
                gameBoard.board[x][y].click();
                counterPlus();
                gameFrame.getScoreBoard()
                        .showMove(currentPlayer().getName() + " middle clicks at (" + y + ", " + x + ").");
            }
        }
        if (lastCounter == counter)
            currentPlayer().Move(gameBoard);
        listenerList.forEach(listener -> listener.onGameStatusChanged(gameStatus, playerlist, isSinglePlayer));
    }

    @Override
    public void onRevealBoard() {
        gameFrame.getBoardComponent().onBoardRevealed(this.gameBoard);
    }

    @Override
    public void onGameRestart() {
        index = 0;
        counter = 0;
        for (int i = 0; i < playerlist.length; i++) {
            playerlist[i].reset();
        }
        gameFrame.getScoreBoard().resetScoreBoard();
        gameStatus = GameStatus.PROGRESSING;
        gameBoard.init(row, column, mineNum);
        if (currentPlayer().isAI())
            currentPlayer().Move(gameBoard);
    }

    @Override
    public void onRecoverBoard() {
        gameFrame.getBoardComponent().onBoardRecovered(this.gameBoard);
        gameBoard.setMineLeft(mineNum);
        index = 0;
        counter = 0;
        for (int i = 0; i < playerlist.length; i++) {
            playerlist[i].reset();
        }
        gameFrame.getScoreBoard().resetScoreBoard();
        gameStatus = GameStatus.PROGRESSING;
        if (currentPlayer().isAI())
            currentPlayer().Move(gameBoard);
    }

    @Override
    public void addListener(GameStatusListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void cancelListener(GameStatusListener listener) {
        listenerList.remove(listener);
    }

    @Override
    public void onMouseUp(int x, int y) {
        gameBoard.getSquare(x, y).isMouseUp = true;
        gameBoard.touchSquare(x, y);
    }

    @Override
    public void onMouseDown(int x, int y) {
        gameBoard.getSquare(x, y).isMouseUp = false;
        gameBoard.touchSquare(x, y);
    }

    // add the index but stil have to use currentPlayer()
    public void nextPlayer() {
        counter = 0;
        index = (index == 3) ? 0 : (index + 1);
        if (currentPlayer().isAI()) {
            currentPlayer().Move(gameBoard);
        }
        gameFrame.getScoreBoard().onPlayerChanged(index);
        return;
    }

    public Player currentPlayer() {
        while (playerlist[index].getAllMoves() == 0) {
            index = (index == 3) ? 0 : (index + 1);
        }
        return playerlist[index];
    }

    public void counterPlus() {
        counter++;
        if (counter == currentPlayer().getAllMoves()) {
            nextPlayer();
        }
        if (currentPlayer().isAI()) {
            gameFrame.getBoardComponent().repaint();
            currentPlayer().Move(gameBoard);
        }
    }

    public void getOnePoint() {
        new ButtonPlayer("game/src/view/sounds/GainScore.wav");
        currentPlayer().gainScore();
        gameFrame.getScoreBoard().onScoreChanged(playerlist);
    }

    public void loseOnePoint() {
        new ButtonPlayer("game/src/view/sounds/Mine.wav");
        currentPlayer().loseScore();
        gameFrame.getScoreBoard().onScoreChanged(playerlist);
    }

    public void makeOneErr() {
        new ButtonPlayer("game/src/view/sounds/Error.wav");
        currentPlayer().makeError();
        currentPlayer().loseScore();
        gameFrame.getScoreBoard().onScoreChanged(playerlist);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Board getBoard() {
        return gameBoard;
    }

    public Player[] getPlayers() {
        return playerlist;
    }

    public int getIndex() {
        return index;
    }

    public int getCounter() {
        return counter;
    }

    public int getMineNum() {
        return this.mineNum;
    }

    public int getMineLeft() {
        return gameBoard.getMineLeft();
    }

    public void setGameBoard(Board board) {
        this.gameBoard = board;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
