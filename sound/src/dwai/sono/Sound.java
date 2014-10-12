package dwai.sono;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Joseph Cumbo
 */
public class Sound {

    public static void main(String[] args) {
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 2, true, true);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            microphone.open(format);
            microphone.start();
            byte[] temp = new byte[2000];
            while (true) {
                microphone.read(temp, 0, temp.length);
                double count = 0;
                double[] fuckit = new double[temp.length];
                for (int i = 0; i < temp.length; i++) {
                    fuckit[i] = temp[i] / 128d;
                    count += Math.abs(temp[i]);
                }
                count = count / (temp.length * 128);
            }
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
