package game.src.view;

import javax.swing.*;

import game.src.display.ButtonPlayer;

import java.awt.event.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private static ImageIcon titileIcon = new ImageIcon("game/src/view/pics/Maintitle.png");
    private static ImageIcon playSound = new ImageIcon("game/src/view/pics/play.png");
    private static JButton newGameBtn = new JButton(new ImageIcon("game/src/view/pics/NewGame.png"));
    private static JButton loadGameBtn = new JButton(new ImageIcon("game/src/view/pics/LoadGame.png"));
    private static JButton quitBtn = new JButton(new ImageIcon("game/src/view/pics/Quit.png"));
    private static JButton stopBgmBtn = new JButton();

    private MainMenu() {
        if (!MainMenuPlayer.getIsPlaying())
            MainMenuPlayer.play();
        playSound.setImage(playSound.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        titileIcon.setImage(titileIcon.getImage().getScaledInstance(700, 690, Image.SCALE_DEFAULT));
        JLabel mainTitleLabel = new JLabel(titileIcon);
        setSize(1000, 700);
        setTitle("MineSweeper v1.0");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        mainTitleLabel.setBounds(0, 0, 700, 690);
        add(mainTitleLabel);

        stopBgmBtn.setIcon(playSound);

        newGameBtn.setBounds(700, 250, 250, 75);
        loadGameBtn.setBounds(700, 400, 250, 75);
        quitBtn.setBounds(700, 550, 250, 75);
        stopBgmBtn.setBounds(900, 20, 50, 50);

        stopBgmBtn.setBorderPainted(true);

        newGameBtn.addActionListener(event -> GameSetter.getGameSetter().setVisible(true));
        newGameBtn.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));

        loadGameBtn.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        loadGameBtn.addActionListener(event -> new GameLoaderFrame());

        quitBtn.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        quitBtn.addActionListener(event -> this.dispose());
        quitBtn.addActionListener(event -> System.exit(0));
        stopBgmBtn.addActionListener(new BgmListener());

        add(newGameBtn);
        add(loadGameBtn);
        add(quitBtn);
        add(stopBgmBtn);

        setVisible(false);
    }

    private static MainMenu Instance = new MainMenu();

    public static MainMenu getInstance() {
        return Instance;
    }

    class BgmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
            if (MainMenuPlayer.getIsPlaying()) {
                MainMenuPlayer.stop();
            } else {
                MainMenuPlayer.play();
            }
        }
    }
}
