package game.src.listener;

import game.src.GameBoard.Square;
import game.src.GameBoard.*;

public interface BoardListener extends Listener {
    void onSquareClicked(Square square);

    void onSquareTouched(Square square);

    void onBoardReload();

    void onBoardRevealed(Board board);

    void onBoardRecovered(Board board);
}
