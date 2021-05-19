package game.src.display;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.*;

import game.src.player.Player;
import game.src.utils.PlayerType;

public class RankingFrame extends JFrame {
    private ArrayList<Player> firstPlayers = new ArrayList<>();
    private JButton close = new JButton("Close");
    private int highestScore;
    private int lowestErr;
    private JLabel firstPlayerLabel = new JLabel("Champion: ");

    private ImageIcon championIcon = new ImageIcon("game/src/display/pics/Champion.png");

    private ImageIcon plainBtnIcon = new ImageIcon("game/src/display/pics/Unclicked.png");

    private RankingFrame() {
        setLayout(null);
        setTitle("GameOver");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        championIcon.setImage(championIcon.getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
        JLabel championLabel = new JLabel(championIcon);
        championLabel.setBounds(5, 30, 70, 70);
        add(championLabel);
        close.setBounds(200, 250, 100, 50);
        close.setHorizontalTextPosition(SwingConstants.CENTER);
        close.setIcon(plainBtnIcon);
        close.addActionListener(event -> this.setVisible(false));
        close.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        add(close);
    }

    private void init(Player[] players) {
        highestScore = -99999;
        lowestErr = 99999;

        plainBtnIcon.setImage(plainBtnIcon.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT));

        for (int i = players.length - 1; i >= 0; i--) {
            if (players[i].getScore() > highestScore && players[i].getType() != PlayerType.CLOSE)
                highestScore = players[i].getScore();
        }

        for (int i = players.length - 1; i >= 0; i--) {
            if (players[i].getScore() == highestScore && players[i].getType() != PlayerType.CLOSE) {
                if (!firstPlayers.contains(players[i])) {
                    firstPlayers.add(players[i]);
                }
            }
        }

        for (int i = 0; i < firstPlayers.size(); i++) {
            if (lowestErr > firstPlayers.get(i).getErrorCount()) {
                lowestErr = firstPlayers.get(i).getErrorCount();
            }
        }

        for (int i = 0; i < firstPlayers.size(); i++) {
            if (firstPlayers.get(i).getErrorCount() > lowestErr)
                firstPlayers.remove(i);
        }

        firstPlayerLabel.setBounds(80, 30, 400, 60);
        for (int i = firstPlayers.size() - 1; i >= 0; i--) {
            firstPlayerLabel.setText(firstPlayerLabel.getText() + firstPlayers.get(i).getName() + "  ");
        }
        firstPlayerLabel.setFont(new Font("Arial Black", Font.PLAIN, 16));
        add(firstPlayerLabel);

    }

    private static RankingFrame instance = new RankingFrame();

    public static RankingFrame getInstance(Player[] players) {
        instance.firstPlayerLabel.setText("Champion: ");
        instance.firstPlayers.clear();
        instance.init(players);
        return instance;
    }
}
