package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent.Type;

public class Sound {

    Clip musicClip;
    URL url[] = new URL[10];

    public Sound() {
    // Use relative path for resources in bin/res/
    url[0] = getClass().getResource("/main/res/trainer red battle theme.wav");
    url[1] = getClass().getResource("/main/res/delete line.wav");
    url[2] = getClass().getResource("/main/res/gameover.wav");
    url[3] = getClass().getResource("/main/res/rotation.wav");
    url[4] = getClass().getResource("/main/res/touch floor.wav");
    }
    public void play(int i, boolean music) { 

        try {
            if (url[i] == null) {
                System.err.println("Audio resource not found for index " + i);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            if(music) {
                musicClip = clip;
            }

            clip.open(ais);
            clip.addLineListener(new LineListener () {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == Type.STOP) {
                        clip.close();
                    }
                } 
            });
            ais.close();
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    public void loop() {
        if (musicClip == null) {
            System.err.println("musicClip is null. Cannot loop music.");
            return;
        }
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        musicClip.stop();
        musicClip.close();
    }
}
