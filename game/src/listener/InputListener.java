package game.src.listener;

import game.src.utils.*;

public interface InputListener extends Listener {
    void onGameRestart();

    void onBoardClicked(int x, int y, ClickType type);

    void onMouseUp(int x, int y);

    void onMouseDown(int x, int y);

    void onRevealBoard();

    void autoBoardClick(int x, int y, ClickType type);

    void onRecoverBoard();
}