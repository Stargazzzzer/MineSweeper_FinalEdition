package game.src.player;

import java.util.ArrayList;

import game.src.GameBoard.Board;
import game.src.GameBoard.Square;
import game.src.display.ButtonPlayer;
import game.src.listener.*;
import game.src.utils.ClickType;
import game.src.utils.PlayerType;

public class Player implements Listenable<InputListener> {
    private PlayerType type;
    private boolean isAI = true;
    private String name;
    private int score = 0;
    private int allmoves;
    private int errorCount = 0;

    private final ArrayList<InputListener> listenerList = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void Move(Board gameBoard) {
        switch (type) {
            case AI_ROOKIE:
                AI_RookieMoves(gameBoard);
                new ButtonPlayer("game/src/view/sounds/Pop.wav");
                break;
            case AI_NORMAL:
                AI_NormalMoves(gameBoard);
                new ButtonPlayer("game/src/view/sounds/Pop.wav");
                break;
            case AI_CRAZY:
                AI_CrazyMoves(gameBoard);
                new ButtonPlayer("game/src/view/sounds/Pop.wav");
                break;
            default:
                break;
        }
    }

    public void AI_RookieMoves(Board board) {
        AIOperator rookie = new AIOperator();
        Square aiClick = rookie.AI_Movement(board, PlayerType.AI_ROOKIE);
        ClickType type = rookie.getAIClick();
        if (aiClick == null)
            listenerList.forEach(listener -> listener.autoBoardClick(999, 999, ClickType.MIDDLE_CLICK));
        else
            listenerList.forEach(listener -> listener.autoBoardClick(aiClick.getX(), aiClick.getY(), type));

    }

    public void AI_NormalMoves(Board board) {
        AIOperator normal = new AIOperator();
        Square aiClick = normal.AI_Movement(board, PlayerType.AI_NORMAL);
        ClickType type = normal.getAIClick();
        if (aiClick == null)
            listenerList.forEach(listener -> listener.autoBoardClick(999, 999, ClickType.MIDDLE_CLICK));
        else
            listenerList.forEach(listener -> listener.autoBoardClick(aiClick.getX(), aiClick.getY(), type));

    }

    public void AI_CrazyMoves(Board board) {
        AIOperator crazy = new AIOperator();
        Square aiClick = crazy.AI_Movement(board, PlayerType.AI_CRAZY);
        ClickType type = crazy.getAIClick();
        if (aiClick == null)
            listenerList.forEach(listener -> listener.autoBoardClick(999, 999, ClickType.MIDDLE_CLICK));
        else
            listenerList.forEach(listener -> listener.autoBoardClick(aiClick.getX(), aiClick.getY(), type));

    }

    public void gainScore() {
        score++;
    }

    public void loseScore() {
        score--;
    }

    public int getScore() {
        return this.score;
    }

    public void setAllMoves(int i) {
        this.allmoves = i;
    }

    public void makeError() {
        errorCount++;
    }

    public String getName() {
        return this.name;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public int getAllMoves() {
        return this.allmoves;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public PlayerType getType() {
        return type;
    }

    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }

    public boolean isAI() {
        return this.isAI;
    }

    public void reset() {
        errorCount = 0;
        score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setErr(int errcount) {
        errorCount = errcount;
    }

    @Override
    public void addListener(InputListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void cancelListener(InputListener listener) {
        listenerList.remove(listener);
    }

}
