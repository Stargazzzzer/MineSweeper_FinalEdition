package game.src.controller;

import java.util.Date;

import game.src.GameBoard.Square;
import game.src.utils.GameStatus;
import javax.swing.JOptionPane;

import java.text.SimpleDateFormat;

import java.io.*;

public class Saver {
    /**
     * Save the game as a new Text file. First record the gameboard: row number,
     * column number, each square's status, playerinfo, index, counter
     * 
     * @param controller
     */
    public Saver(GameController controller) {
        if (controller.getGameStatus() != GameStatus.PROGRESSING) {
            JOptionPane.showMessageDialog(null, "Error: Save failed! The game is ended!", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
        String currentTimeStr = df.format(new Date());
        File newFile = new File("game/src/controller/out/" + controller.currentPlayer().getName() + "_" + currentTimeStr
                + "SaveData.txt");
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
            // first line: header
            out.write(currentTimeStr);

            out.write("\n");

            // next line: row column mineNum mineLeft squarecounts

            out.write(Integer.toString(controller.getRow()));
            out.write(" ");
            out.write(Integer.toString(controller.getColumn()));
            out.write(" ");
            out.write(Integer.toString(controller.getMineNum()));
            out.write(" ");
            out.write(Integer.toString(controller.getBoard().getMineLeft()));
            out.write(" ");
            out.write(Integer.toString(controller.getBoard().getSquareCount()));
            out.write("\n");

            // next line: PlayerType PlayerScore PlayerError Allmoves
            for (int i = 0; i < controller.getPlayers().length; i++) {
                out.write(controller.getPlayers()[i].getType().toString());
                out.write(" ");
                out.write(Integer.toString(controller.getPlayers()[i].getScore()));
                out.write(" ");
                out.write(Integer.toString(controller.getPlayers()[i].getErrorCount()));
                out.write(" ");
                out.write(Integer.toString(controller.getPlayers()[i].getAllMoves()));
                out.write("\n");
            }

            // next line: square info
            // info = status isClicked isFlagged
            for (int i = 0; i < controller.getRow(); i++) {
                for (int j = 0; j < controller.getColumn(); j++) {
                    Square tempSquare = controller.getBoard().getSquare(i, j);
                    out.write(Integer.toString(tempSquare.getStatus()));
                    out.write(" ");
                    if (tempSquare.isClicked())
                        out.write("1");
                    else
                        out.write("0");

                    out.write(" ");

                    if (tempSquare.isFlagged())
                        out.write("1");
                    else
                        out.write("0");

                    out.write("\n");
                }
            }

            // next line: index
            out.write(Integer.toString(controller.getIndex()));
            out.write("\n");

            // next line: counter
            out.write(Integer.toString(controller.getCounter()));

            // saving action succeeded
            out.close();
            JOptionPane.showMessageDialog(null, "Save Success!", "", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Saver(Loader loader) {
        if (loader.getGameStatus() == GameStatus.END) {
            JOptionPane.showMessageDialog(null, "Error: Save failed! The game is ended!", "Error",
                    JOptionPane.PLAIN_MESSAGE);
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
        String currentTimeStr = df.format(new Date());
        File newFile = new File("game/src/controller/out/" + currentTimeStr + "SaveData.txt");
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
            // first line: header
            out.write(currentTimeStr);

            out.write("\n");

            // next line: row column mineNum mineLeft squarecount moves

            out.write(Integer.toString(loader.getRow()));
            out.write(" ");
            out.write(Integer.toString(loader.getColumn()));
            out.write(" ");
            out.write(Integer.toString(loader.getMineNum()));
            out.write(" ");
            out.write(Integer.toString(loader.getBoard().getMineLeft()));
            out.write(" ");
            out.write(Integer.toString(loader.getBoard().getSquareCount()));
            out.write("\n");

            // next line: PlayerType PlayerScore PlayerError Allmoves
            for (int i = 0; i < loader.getPlayers().length; i++) {
                out.write(loader.getPlayers()[i].getType().toString());
                out.write(" ");
                out.write(Integer.toString(loader.getPlayers()[i].getScore()));
                out.write(" ");
                out.write(Integer.toString(loader.getPlayers()[i].getErrorCount()));
                out.write(" ");
                out.write(Integer.toString(loader.getPlayers()[i].getAllMoves()));
                out.write("\n");
            }

            // next line: square info
            // info = status isClicked isFlagged
            for (int i = 0; i < loader.getRow(); i++) {
                for (int j = 0; j < loader.getColumn(); j++) {
                    Square tempSquare = loader.getBoard().getSquare(i, j);
                    out.write(Integer.toString(tempSquare.getStatus()));
                    out.write(" ");
                    if (tempSquare.isClicked())
                        out.write("1");
                    else
                        out.write("0");

                    out.write(" ");

                    if (tempSquare.isFlagged())
                        out.write("1");
                    else
                        out.write("0");

                    out.write("\n");
                }
            }

            // next line: index
            out.write(Integer.toString(loader.getIndex()));
            out.write("\n");

            // next line: counter
            out.write(Integer.toString(loader.getCounter()));

            // saving action succeeded
            out.close();
            JOptionPane.showMessageDialog(null, "Save Success!", "", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
