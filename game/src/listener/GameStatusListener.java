package game.src.listener;

import game.src.player.Player;
import game.src.utils.GameStatus;

public interface GameStatusListener extends Listener {
    void onGameStatusChanged(GameStatus status, Player[] players, boolean isSinglePlayer);
}
