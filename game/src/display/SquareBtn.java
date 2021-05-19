package game.src.display;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.*;

public class SquareBtn extends JButton {
    public static ImageIcon unclicked = new ImageIcon("game/src/display/pics/Unclicked.png");
    public static ImageIcon unclickedLight = new ImageIcon("game/src/display/pics/UnclickedLight.png");
    public static ImageIcon number0 = new ImageIcon("game/src/display/pics/Clicked0.png");
    public static ImageIcon number1 = new ImageIcon("game/src/display/pics/Clicked1.png");
    public static ImageIcon number2 = new ImageIcon("game/src/display/pics/Clicked2.png");
    public static ImageIcon number3 = new ImageIcon("game/src/display/pics/Clicked3.png");
    public static ImageIcon number4 = new ImageIcon("game/src/display/pics/Clicked4.png");
    public static ImageIcon number5 = new ImageIcon("game/src/display/pics/Clicked5.png");
    public static ImageIcon number6 = new ImageIcon("game/src/display/pics/Clicked6.png");
    public static ImageIcon number7 = new ImageIcon("game/src/display/pics/Clicked7.png");
    public static ImageIcon number8 = new ImageIcon("game/src/display/pics/Clicked8.png");
    public static ImageIcon number_1 = new ImageIcon("game/src/display/pics/Clicked-1.png");
    public static ImageIcon flagicon = new ImageIcon("game/src/display/pics/flag.png");
    public static ImageIcon unclickedMine = new ImageIcon("game/src/display/pics/UnclickedMine.png");

    public SquareBtn(int size) {
        setMargin(new Insets(0, 0, 0, 0));
        setIcon(unclicked);
        setSize(size, size);
        setVisible(true);
        setLayout(null);
    }

    public static void initIcon(int squareSize) {
        unclicked.setImage(unclicked.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        unclickedLight.setImage(unclickedLight.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number_1.setImage(number_1.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number0.setImage(number0.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number1.setImage(number1.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number2.setImage(number2.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number3.setImage(number3.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number4.setImage(number4.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number5.setImage(number5.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number6.setImage(number6.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number7.setImage(number7.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        number8.setImage(number8.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        flagicon.setImage(flagicon.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
        unclickedMine.setImage(unclickedMine.getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));

    }
}
