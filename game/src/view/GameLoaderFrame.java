package game.src.view;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import game.src.controller.Loader;
import game.src.display.ButtonPlayer;
import game.src.display.GamePlayer;

public class GameLoaderFrame extends JFrame {
    private JComboBox<String> selectBox;
    private JLabel header = new JLabel("Please choose a valid file to load: ");

    private ImageIcon plainBtnIcon = new ImageIcon("game/src/display/pics/Unclicked.png");

    private File data = new File("game/src/controller/out");

    private JButton startBtn = new JButton("Start");
    private JButton cancelBtn = new JButton("Cancel");
    private JButton deleteBtn = new JButton("Delete");

    public GameLoaderFrame() {
        setTitle("Load");
        setSize(470, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        String[] dataList = data.list();

        header.setBounds(50, 30, 370, 20);
        add(header);

        selectBox = new JComboBox<String>(dataList);
        selectBox.setBounds(50, 60, 370, 30);

        add(selectBox);

        plainBtnIcon.setImage(plainBtnIcon.getImage().getScaledInstance(100, 45, Image.SCALE_DEFAULT));

        startBtn.setIcon(plainBtnIcon);
        deleteBtn.setIcon(plainBtnIcon);
        cancelBtn.setIcon(plainBtnIcon);

        startBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        cancelBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        startBtn.setBounds(50, 150, 100, 45);
        deleteBtn.setBounds(180, 150, 100, 45);
        cancelBtn.setBounds(310, 150, 100, 45);

        add(startBtn);
        add(deleteBtn);
        add(cancelBtn);

        startBtn.addActionListener(new LoadListener());
        startBtn.addActionListener(event -> this.dispose());

        deleteBtn.addActionListener(new LoadListener());

        cancelBtn.addActionListener(event -> new ButtonPlayer("game/src/view/sounds/MenuClick.wav"));
        cancelBtn.addActionListener(event -> this.dispose());

        setVisible(true);
    }

    class LoadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ButtonPlayer("game/src/view/sounds/MenuClick.wav");
            if (e.getSource() == startBtn) {
                if (selectBox.getItemCount() != 0) {
                    MainMenu.getInstance().setExtendedState(JFrame.ICONIFIED);
                    File file = new File("game/src/controller/out/" + selectBox.getSelectedItem().toString());
                    if (file.exists())
                        new Loader(selectBox.getSelectedItem().toString());
                    else {
                        JOptionPane.showMessageDialog(null, "No such file!", "Error", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    MainMenuPlayer.stop();
                    if (!GamePlayer.getIsPlaying()) {
                        GamePlayer.play();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "There is no file!", "Error", JOptionPane.PLAIN_MESSAGE);
                }
            }

            if (e.getSource() == deleteBtn) {
                File toDelete = new File("game/src/controller/out/" + selectBox.getSelectedItem().toString());
                if (toDelete.exists()) {
                    toDelete.delete();
                    JOptionPane.showMessageDialog(null, "File deleted!", "", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }
}
