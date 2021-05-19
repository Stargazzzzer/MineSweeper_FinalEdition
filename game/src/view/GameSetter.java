package game.src.view;

import java.util.ArrayList;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import game.src.utils.*;
import game.src.controller.*;
import game.src.display.ButtonPlayer;
import game.src.display.GamePlayer;
import game.src.listener.InputListener;
import game.src.listener.Listenable;

public class GameSetter extends JFrame implements Listenable<InputListener> {
    private ArrayList<PlayerType> playerType = new ArrayList<>();
    private ArrayList<JComboBox<String>> playerSetJCB = new ArrayList<>();

    private final ArrayList<InputListener> listenerList = new ArrayList<>();

    private int row;
    private int column;
    private int mineNum;
    private int moves;

    private GameSetter() {
        setSize(470, 350);
        setTitle("Setting");
        setLocationRelativeTo(null);
        setLayout(null);

        // set JComboBox
        for (int i = 0; i <= 3; i++) {
            playerSetJCB.add(new JComboBox<String>());
            playerType.add(PlayerType.CLOSE);
            playerSetJCB.get(i).addItem("Close");
            playerSetJCB.get(i).addItem("Player");
            playerSetJCB.get(i).addItem("AI - Rookie");
            playerSetJCB.get(i).addItem("AI - Normal");
            playerSetJCB.get(i).addItem("AI - Crazy");
            playerSetJCB.get(i).setBounds(15, 50 + i * 50, 100, 30);

            this.add(playerSetJCB.get(i));
            setListener(i);
            this.repaint();
        }

        GameSetArea gameSetArea = new GameSetArea();
        add(gameSetArea);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(false);
        setResizable(false);
        repaint();
    }

    private static GameSetter gameSetter = new GameSetter();

    public static GameSetter getGameSetter() {
        return gameSetter;
    }

    // set ItemLstener
    private void setListener(int index) {
        playerSetJCB.get(index).addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                switch (event.getStateChange()) {
                    case ItemEvent.SELECTED:
                        new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
                        if (event.getItem().equals("Player")) {
                            playerType.set(index, PlayerType.PLAYER);
                        }
                        if (event.getItem().equals("AI - Rookie")) {
                            playerType.set(index, PlayerType.AI_ROOKIE);

                        }
                        if (event.getItem().equals("AI - Normal")) {
                            playerType.set(index, PlayerType.AI_NORMAL);
                        }
                        if (event.getItem().equals("AI - Crazy")) {
                            playerType.set(index, PlayerType.AI_CRAZY);
                        }
                        if (event.getItem().equals("Close")) {
                            playerType.set(index, PlayerType.CLOSE);
                        }
                        repaint();
                        break;
                    case ItemEvent.DESELECTED:
                        break;
                }
            }
        });
    }

    class GameSetArea extends JComponent {
        private JTextArea rowTextArea = new JTextArea("Input Row (6 ~ 24)");
        private JTextArea columnTextArea = new JTextArea("Input Column (6 ~ 30)");
        private JTextArea mineNumTextArea = new JTextArea("Input number of mine");
        private JTextArea movesEachTurnTextArea = new JTextArea("Input moves per turn\n\t(1 ~ 5)");

        private JLabel playerLabel = new JLabel("Set players:");
        private JLabel defaultLabel = new JLabel("Use Defaults:");
        private JLabel parameterLable = new JLabel("Customize parameters:");

        private ImageIcon plainBtnIcon = new ImageIcon("game/src/display/pics/Unclicked.png");
        private ImageIcon startIcon = new ImageIcon("game/src/display/pics/Unclicked.png");

        private JButton eazy = new JButton("Eazy");
        private JButton medium = new JButton("Medium");
        private JButton hard = new JButton("Hard");

        private JButton startGameBtn = new JButton("Start");

        GameSetArea() {
            setBounds(0, 0, 470, 350);
            rowTextArea.setBounds(280, 50, 160, 35);
            rowTextArea.setFont(textAreaFont);
            columnTextArea.setBounds(280, 100, 160, 35);
            columnTextArea.setFont(textAreaFont);
            mineNumTextArea.setBounds(280, 150, 160, 35);
            mineNumTextArea.setFont(textAreaFont);
            movesEachTurnTextArea.setBounds(280, 200, 160, 35);
            movesEachTurnTextArea.setFont(textAreaFont);

            eazy.setBounds(175, 50, 80, 40);
            medium.setBounds(175, 100, 80, 40);
            hard.setBounds(175, 150, 80, 40);

            plainBtnIcon.setImage(plainBtnIcon.getImage().getScaledInstance(80, 40, Image.SCALE_DEFAULT));

            eazy.setIcon(plainBtnIcon);
            medium.setIcon(plainBtnIcon);
            hard.setIcon(plainBtnIcon);

            eazy.setHorizontalTextPosition(SwingConstants.CENTER);
            medium.setHorizontalTextPosition(SwingConstants.CENTER);
            hard.setHorizontalTextPosition(SwingConstants.CENTER);

            eazy.addActionListener(new EazyListener());
            medium.addActionListener(new MediumListener());
            hard.addActionListener(new HardListener());

            startIcon.setImage(plainBtnIcon.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT));
            startGameBtn.setIcon(startIcon);
            startGameBtn.setHorizontalTextPosition(SwingConstants.CENTER);

            startGameBtn.setBounds(240, 250, 100, 50);
            startGameBtn.addActionListener(new GameStartListener());

            playerLabel.setBounds(15, 20, 100, 30);
            defaultLabel.setBounds(175, 20, 80, 30);
            parameterLable.setBounds(280, 20, 160, 30);

            add(playerLabel);
            add(defaultLabel);
            add(parameterLable);

            add(rowTextArea);
            add(columnTextArea);
            add(mineNumTextArea);
            add(movesEachTurnTextArea);

            add(eazy);
            add(medium);
            add(hard);

            add(startGameBtn);

            setVisible(true);
            repaint();
        }

        private boolean settingsCheck(JTextArea area) {
            boolean isLegal = true;
            for (int i = 0; i < area.getText().length(); i++) {
                if (area.getText() == "" | !Character.isDigit(area.getText().charAt(i)) | area.getText().length() > 4) {
                    isLegal = false;
                }
            }
            return isLegal;
        }

        private boolean playerCheck(ArrayList<PlayerType> playerType) {
            boolean isLegal = true;
            int count = 0;
            for (int i = 0; i < 4; i++) {
                if (playerType.get(i) == PlayerType.CLOSE) {
                    count++;
                }
            }
            if (count == 4)
                isLegal = false;

            return isLegal;
        }

        // Listeners
        class EazyListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
                rowTextArea.setText("9");
                columnTextArea.setText("9");
                mineNumTextArea.setText("10");
                movesEachTurnTextArea.setText("1");
            }
        }

        class MediumListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
                rowTextArea.setText("16");
                columnTextArea.setText("16");
                mineNumTextArea.setText("40");
                movesEachTurnTextArea.setText("3");
            }
        }

        class HardListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
                rowTextArea.setText("16");
                columnTextArea.setText("30");
                mineNumTextArea.setText("99");
                movesEachTurnTextArea.setText("5");
            }
        }

        class GameStartListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
                boolean b = false;
                if (playerCheck(playerType)) {
                    if (settingsCheck(rowTextArea) && settingsCheck(columnTextArea) && settingsCheck(mineNumTextArea)
                            && settingsCheck(movesEachTurnTextArea)) {
                        if (Integer.parseInt(rowTextArea.getText()) <= 24
                                && Integer.parseInt(rowTextArea.getText()) >= 6) {
                            if (Integer.parseInt(columnTextArea.getText()) <= 30
                                    && Integer.parseInt(columnTextArea.getText()) >= 6) {

                                if (Integer.parseInt(movesEachTurnTextArea.getText()) <= 5
                                        && Integer.parseInt(movesEachTurnTextArea.getText()) >= 1) {
                                    row = Integer.parseInt(rowTextArea.getText());
                                    column = Integer.parseInt(columnTextArea.getText());
                                    moves = Integer.parseInt(movesEachTurnTextArea.getText());
                                    mineNum = Integer.parseInt(mineNumTextArea.getText());
                                    if (Integer.parseInt(mineNumTextArea.getText()) < (row * column / 2)) {
                                        b = true;
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Please check the number of mine!", "Error",
                                                JOptionPane.PLAIN_MESSAGE);
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(null, "Please check the set of moves for each turn!",
                                            "Error", JOptionPane.PLAIN_MESSAGE);
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "Please check the column number!", "Error",
                                        JOptionPane.PLAIN_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please check the row number!", "Error",
                                    JOptionPane.PLAIN_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please check the inputs!", "Error",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "The game must involve at least one player!", "Error",
                            JOptionPane.PLAIN_MESSAGE);
                }

                if (b) {
                    // NewGame
                    MainMenuPlayer.stop();
                    new GameController(playerType, row, column, mineNum, moves);
                    if (!GamePlayer.getIsPlaying()) {
                        GamePlayer.play();
                    }
                    getGameSetter().setVisible(false);
                    MainMenu.getInstance().setExtendedState(JFrame.ICONIFIED);
                }
            }
        }

        // Getters
        public ArrayList<PlayerType> getPlayerInfo() {
            return playerType;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public int getMineNum() {
            return mineNum;
        }

        public int getMoves() {
            return moves;
        }

        // fonts
        Font textAreaFont = new Font("TimesRoman", Font.PLAIN, 14);

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