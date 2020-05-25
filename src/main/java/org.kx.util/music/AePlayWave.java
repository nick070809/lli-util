package org.kx.util.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/22
 */

public class AePlayWave extends Thread {

    private String filename;

    public AePlayWave(String wavfile) {
        filename = wavfile;

    }

    public void run() {

        File soundFile = new File(filename);

        AudioInputStream audioInputStream = null;
        try {
           /* AudioFileFormat aff = AudioSystem.getAudioFileFormat(soundFile);
            AudioInputStream in= AudioSystem.getAudioInputStream(soundFile);*/

            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[512];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            auline.drain();
            auline.close();
        }

    }


    public static void main(String[] args) {
        AePlayWave apw=new AePlayWave("/Users/xianguang/Downloads/1.mp3");
        apw.start();
    }
}
