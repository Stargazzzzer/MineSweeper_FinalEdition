package game.src.view;

import java.io.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.*;

import game.src.controller.Loader;
import game.src.display.ButtonPlayer;
import game.src.display.GamePlayer;

public class GameLoaderFrame extends JFrame {
    private JComboBox<String> selectBox;
    private JLabel header = new JLabel("Please choose a valid file to load: ");
    private JButton startBtn = new JButton("Start");
    private JButton cancelBtn = new JButton("Cancel");

    public GameLoaderFrame() {
        setTitle("Load");
        setSize(470, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        File data = new File("game/src/controller/out");
        String[] dataList = data.list();

        header.setBounds(50, 30, 370, 20);
        add(header);

        selectBox = new JComboBox<String>(dataList);
        selectBox.setBounds(50, 60, 370, 30);

        add(selectBox);

        startBtn.setBounds(55, 150, 100, 45);
        cancelBtn.setBounds(470 - 150 - 20, 150, 100, 45);

        add(startBtn);
        add(cancelBtn);

        startBtn.addActionListener(new StartListener());
        startBtn.addActionListener(event -> this.dispose());

        cancelBtn.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        cancelBtn.addActionListener(event -> this.dispose());

        setVisible(true);
    }

    class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
            if (selectBox.getItemCount() != 0) {
                MainMenu.getInstance().setExtendedState(JFrame.ICONIFIED);
                new Loader(selectBox.getSelectedItem().toString());
                MainMenuPlayer.stop();
                if (!GamePlayer.getIsPlaying()) {
                    GamePlayer.play();
                }
            } else {
                JOptionPane.showMessageDialog(null, "There is no file!", "Error", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }
}
