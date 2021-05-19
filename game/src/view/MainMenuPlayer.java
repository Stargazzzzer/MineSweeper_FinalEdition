package game.src.view;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class MainMenuPlayer {
    private static Clip bgm;
    private static AudioInputStream ais;
    private static boolean isPlaying = false;

    MainMenuPlayer() {
    }

    public static void play() {
        isPlaying = true;
        try {
            bgm = AudioSystem.getClip();
            InputStream is = MainMenuPlayer.class.getClassLoader()
                    .getResourceAsStream("game/src/view/sounds/The Green Kingdom - Untitled.wav");
            if (is != null) {
                ais = AudioSystem.getAudioInputStream(is);
            }
            bgm.open(ais);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        bgm.start();
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-5.0f);
    }

    public static void stop() {
        isPlaying = false;
        if (ais != null)
            bgm.close();
    }

    public static boolean getIsPlaying() {
        return isPlaying;
    }
}