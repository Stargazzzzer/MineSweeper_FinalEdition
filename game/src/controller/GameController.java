package game.src.controller;

import java.util.ArrayList;

import game.src.utils.GameStatus;
import game.src.utils.PlayerType;
import game.src.player.*;
import game.src.GameBoard.*;
import game.src.display.ButtonPlayer;
import game.src.display.GameFrame;
import game.src.utils.ClickType;
import game.src.listener.*;

public class GameController implements InputListener, Listenable<GameStatusListener> {
    public int counter = 0; // count the moves taken
    private boolean isSinglePlayer = false;
    private int row;
    private int column;
    private GameStatus gameStatus;
    private int mineNum;
    private int index; // represents which player is acting in the playerList
    private Player[] playerlist = new Player[4];
    private Board gameBoard;
    private final GameFrame gameFrame;

    private final ArrayList<GameStatusListener> listenerList = new ArrayList<>();

    public GameController(ArrayList<PlayerType> playerType, int row, int column, int mineNum, int moves) {
        gameBoard = new Board(row, column, mineNum);
        this.row = row;
        this.column = column;
        this.gameStatus = GameStatus.PROGRESSING;
        this.mineNum = mineNum;
        index = 0;
        // add Players
        for (int i = 0; i < playerType.size(); i++) {
            switch (playerType.get(i)) {
                case PLAYER:
                    playerlist[i] = new Player("Player" + Integer.toString(i + 1));
                    playerlist[i].setAllMoves(moves);
                    playerlist[i].setIsAI(false);
                    playerlist[i].setType(playerType.get(i));
                    break;
                case AI_ROOKIE:
                    playerlist[i] = new Player("AI_Rookie" + Integer.toString(i + 1));
                    playerlist[i].setType(PlayerType.AI_ROOKIE);
                    playerlist[i].setIsAI(true);
                    playerlist[i].setAllMoves(moves);
                    playerlist[i].addListener(this);
                    playerlist[i].setType(playerType.get(i));
                    break;
                case AI_NORMAL:
                    playerlist[i] = new Player("AI_Normal" + Integer.toString(i + 1));
                    playerlist[i].setIsAI(true);
                    playerlist[i].setAllMoves(moves);
                    playerlist[i].addListener(this);
                    playerlist[i].setType(playerType.get(i));
                    break;
                case AI_CRAZY:
                    playerlist[i] = new Player("AI_Crazy" + Integer.toString(i + 1));
                    playerlist[i].setIsAI(true);
                    playerlist[i].setAllMoves(moves);
                    playerlist[i].addListener(this);
                    playerlist[i].setType(playerType.get(i));
                    break;
                case CLOSE:
                    playerlist[i] = new Player("Closed");
                    playerlist[i].setIsAI(false);
                    playerlist[i].setAllMoves(0);
                    playerlist[i].setType(playerType.get(i));
                default:
                    break;
            }
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
        onGameRestart();
        addListener(gameFrame.getBoardComponent());

        // switch to the closest Player
        while (playerType.get(index) == PlayerType.CLOSE) {
            nextPlayer();
        }
        if (currentPlayer().isAI())
            currentPlayer().Move(gameBoard);
        gameFrame.getSaveBtn().addActionListener(event -> new Saver(this));
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
     * Same method for ai to click
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
                index = 0;
                counter = 0;
                for (int i = 0; i < playerlist.length; i++) {
                    playerlist[i].reset();
                }
                gameFrame.getScoreBoard().resetScoreBoard();
                gameStatus = GameStatus.PROGRESSING;
                gameBoard.init(row, column, mineNum);
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
    public void onMouseUp(int x, int y) {
        gameBoard.getSquare(x, y).isMouseUp = true;
        gameBoard.touchSquare(x, y);
    }

    @Override
    public void onMouseDown(int x, int y) {
        gameBoard.getSquare(x, y).isMouseUp = false;
        gameBoard.touchSquare(x, y);
    }

    @Override
    public void addListener(GameStatusListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void cancelListener(GameStatusListener listener) {
        listenerList.remove(listener);
    }

    // add the index but stil have to use currentPlayer()
    public void nextPlayer() {
        counter = 0;
        // change?
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
            currentPlayer().Move(gameBoard);
            gameFrame.getBoardComponent().repaint();

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
