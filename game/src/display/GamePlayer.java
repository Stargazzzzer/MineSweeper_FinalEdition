package game.src.display;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class GamePlayer {
    private static Clip bgm;
    private static AudioInputStream ais;
    private static boolean isPlaying = false;

    GamePlayer() {
    }

    public static void play() {
        isPlaying = true;
        try {
            bgm = AudioSystem.getClip();
            InputStream is = GamePlayer.class.getClassLoader()
                    .getResourceAsStream("game/src/view/sounds/Mitch Murder - Frantic Aerobics.wav");
            if (is != null)
                ais = AudioSystem.getAudioInputStream(is);
            bgm.open(ais);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        bgm.start();
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-10.0f);
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