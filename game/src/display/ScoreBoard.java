package game.src.display;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

import game.src.listener.ScoreBoardListener;
import game.src.player.Player;
import game.src.utils.PlayerType;

public class ScoreBoard extends JFrame implements ScoreBoardListener {
    private JLabel[] playerScoreJLabel = new JLabel[4];
    private JLabel[] playerErrorJLabel = new JLabel[4];
    private JLabel noticeLabel = new JLabel(new ImageIcon("game\\src\\display\\pics\\NoticeIcon.png"));
    private JLabel firstPlaceLabel = new JLabel(new ImageIcon("game\\src\\display\\pics\\Crown.png"));
    private JTextArea recordArea = new JTextArea();
    private static JButton stopBgmBtn;
    private static ImageIcon playSound = new ImageIcon("game/src/view/pics/play.png");

    private Border border = BorderFactory.createLineBorder(Color.BLACK, 3);

    public ScoreBoard(Player[] players) {
        stopBgmBtn = new JButton();
        setTitle("ScoreBoard");
        setSize(290, 760);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        noticeLabel.setBounds(0, 50, 50, 50);
        firstPlaceLabel.setBounds(40, 23, 95, 10);
        recordArea.setBounds(10, 610, 260, 130);
        recordArea.setFont(new Font("Arial Black", Font.PLAIN, 13));
        recordArea.setEditable(false);
        add(firstPlaceLabel);
        add(noticeLabel);
        add(recordArea);
        setLayout(null);

        playSound.setImage(playSound.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        stopBgmBtn.setBounds(235, 20, 40, 40);
        stopBgmBtn.setIcon(playSound);
        stopBgmBtn.addActionListener(new BgmListener());
        add(stopBgmBtn);

        for (int i = 0; i < players.length; i++) {
            JLabel playerNamelJLabel = new JLabel(players[i].getName());
            playerNamelJLabel.setFont(new Font("TimesRoman", Font.BOLD, 15));
            playerNamelJLabel.setBorder(border);
            playerNamelJLabel.setBounds(50, i * 150, 185, 149);
            this.add(playerNamelJLabel);
            repaint();
        }

        for (int j = 0; j < 4; j++) {
            playerScoreJLabel[j] = new JLabel();
            playerScoreJLabel[j].setText("Score: " + Integer.toString(players[j].getScore()));
            playerScoreJLabel[j].setFont(new Font("TimesRoman", Font.BOLD, 15));
            playerScoreJLabel[j].setBounds(150, j * 150, 100, 75);
            this.add(playerScoreJLabel[j]);
            repaint();
        }

        for (int k = 0; k < 4; k++) {
            playerErrorJLabel[k] = new JLabel();
            playerErrorJLabel[k].setText("Error: " + Integer.toString(players[k].getErrorCount()));
            playerErrorJLabel[k].setFont(new Font("TimesRoman", Font.BOLD, 15));
            playerErrorJLabel[k].setBounds(150, 75 + k * 150, 100, 75);
            this.add(playerErrorJLabel[k]);
            repaint();
        }
        setVisible(true);
    }

    @Override
    public void onScoreChanged(Player[] players) {
        int index = 0;
        for (int j = 0; j < 4; j++) {
            playerScoreJLabel[j].setText("Score: " + Integer.toString(players[j].getScore()));
            playerScoreJLabel[j].setFont(new Font("TimesRoman", Font.BOLD, 15));
            repaint();
        }
        for (int k = 0; k < 4; k++) {
            playerErrorJLabel[k].setText("Error: " + Integer.toString(players[k].getErrorCount()));
            playerErrorJLabel[k].setFont(new Font("TimesRoman", Font.BOLD, 15));
            repaint();
        }

        // crown
        // calculate the first player with the highest score's index
        for (int i = 3; i >= 0; i--) {
            if (players[index].getScore() <= players[i].getScore() && players[i].getType() != PlayerType.CLOSE) {
                index = i;
            }
        }

        for (int i = 3; i >= 0; i--) {
            if (players[i].getScore() == players[index].getScore() && players[i].getType() != PlayerType.CLOSE) {
                if (players[i].getErrorCount() <= players[index].getErrorCount()
                        && players[i].getType() != PlayerType.CLOSE) {
                    index = i;
                }
            }
        }

        firstPlaceLabel.setLocation(40, 10 + index * 150);
    }

    @Override
    public void onPlayerChanged(int index) {
        noticeLabel.setLocation(0, index * 150 + 50);
        repaint();
    }

    public void resetScoreBoard() {
        firstPlaceLabel.setBounds(40, 23, 95, 10);
        noticeLabel.setBounds(0, 50, 50, 50);
        for (int j = 0; j < 4; j++) {
            playerScoreJLabel[j].setText("Score: " + 0);
            repaint();
        }
        for (int k = 0; k < 4; k++) {
            playerErrorJLabel[k].setText("Error: " + 0);
            repaint();
        }
        recordArea.setText("");
        repaint();
    }

    public void showMove(String text) {
        String[] temp = recordArea.getText().split("\n");
        if (temp.length >= 5) {
            temp[0] = temp[1];
            temp[1] = temp[2];
            temp[2] = temp[3];
            temp[3] = temp[4];
            temp[4] = text;
            recordArea.setText(temp[0]);
            recordArea.setText(recordArea.getText() + "\n" + temp[1]);
            recordArea.setText(recordArea.getText() + "\n" + temp[2]);
            recordArea.setText(recordArea.getText() + "\n" + temp[3]);
            recordArea.setText(recordArea.getText() + "\n" + temp[4]);

        } else {
            if (recordArea.getText().equals(""))
                recordArea.setText(text);
            else
                recordArea.setText(recordArea.getText() + "\n" + text);
        }
    }

    class BgmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
            if (GamePlayer.getIsPlaying()) {
                GamePlayer.stop();
            } else {
                GamePlayer.play();
            }
        }
    }

}
