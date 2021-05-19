package game.src.display;

import javax.swing.*;

import game.src.listener.InputListener;
import game.src.listener.Listenable;
import game.src.player.Player;
import game.src.view.MainMenu;
import game.src.view.MainMenuPlayer;

import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;

public class GameFrame extends JFrame implements Listenable<InputListener> {
    private BoardComponent boardComponent;
    private ScoreBoard scoreBoard;
    private final ArrayList<InputListener> listenerList = new ArrayList<>();

    private ImageIcon plainBtnIcon = new ImageIcon("game/src/display/pics/Unclicked.png");

    private JButton restart = new JButton("Restart");
    private JButton recover = new JButton("Recover");
    private JButton save = new JButton("Save");
    private JButton cheat = new JButton("Cheat");
    private JButton quitgame = new JButton("Quit Game");

    public GameFrame(int row, int column, Player[] player) {
        scoreBoard = new ScoreBoard(player);
        setTitle("MineSweeper");
        int squareSize = Math.min(740 / row, 1200 / column);
        setSize(column * squareSize + 23, row * squareSize + 100);
        setLayout(null);
        setResizable(false);

        int distance = column * squareSize / 19;

        plainBtnIcon.setImage(plainBtnIcon.getImage().getScaledInstance(3 * distance, 40, Image.SCALE_DEFAULT));

        recover.setBounds(2 * distance, 10, 3 * distance, 20);
        restart.setBounds(2 * distance, 30, 3 * distance, 20);
        save.setBounds(6 * distance, 10, 3 * distance, 40);
        cheat.setBounds(10 * distance, 10, 3 * distance, 40);
        quitgame.setBounds(14 * distance, 10, 3 * distance, 40);

        recover.setHorizontalTextPosition(SwingConstants.CENTER);
        restart.setHorizontalTextPosition(SwingConstants.CENTER);
        save.setHorizontalTextPosition(SwingConstants.CENTER);
        cheat.setHorizontalTextPosition(SwingConstants.CENTER);
        quitgame.setHorizontalTextPosition(SwingConstants.CENTER);

        restart.setIcon(plainBtnIcon);
        save.setIcon(plainBtnIcon);
        cheat.setIcon(plainBtnIcon);
        quitgame.setIcon(plainBtnIcon);
        recover.setIcon(plainBtnIcon);

        recover.setMargin(new Insets(0, 0, 0, 0));
        restart.setMargin(new Insets(0, 0, 0, 0));
        save.setMargin(new Insets(0, 0, 0, 0));
        cheat.setMargin(new Insets(0, 0, 0, 0));
        quitgame.setMargin(new Insets(0, 0, 0, 0));

        recover.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        recover.addActionListener(event -> listenerList.forEach(InputListener::onRecoverBoard));
        recover.addActionListener(event -> boardComponent.onBoardReload());

        quitgame.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        quitgame.addActionListener(event -> this.dispose());
        quitgame.addActionListener(new ReturnToMenuListener());

        restart.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        restart.addActionListener(event -> listenerList.forEach(InputListener::onGameRestart));

        cheat.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        cheat.addActionListener(event -> listenerList.forEach(InputListener::onRevealBoard));

        add(restart);
        add(save);
        add(quitgame);
        add(cheat);
        add(recover);
        boardComponent = new BoardComponent(row, column, squareSize);
        add(boardComponent);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public BoardComponent getBoardComponent() {
        return boardComponent;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public JButton getSaveBtn() {
        return save;
    }

    @Override
    public void addListener(InputListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void cancelListener(InputListener listener) {
        listenerList.remove(listener);
    }

    class ReturnToMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            scoreBoard.dispose();
            MainMenu.getInstance().setExtendedState(NORMAL);
            GamePlayer.stop();
            if (!MainMenuPlayer.getIsPlaying()) {
                MainMenuPlayer.play();
            }
        }
    }
}
