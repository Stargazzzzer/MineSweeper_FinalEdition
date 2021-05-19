package game.src.listener;
import game.src.player.*;

public interface ScoreBoardListener extends Listener{
    void onScoreChanged(Player[] player);
    void onPlayerChanged(int index);
}
