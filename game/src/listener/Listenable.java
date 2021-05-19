package game.src.listener;

public interface Listenable<T extends Listener> {
    void addListener(T listener);
    void cancelListener(T listener);   
}
