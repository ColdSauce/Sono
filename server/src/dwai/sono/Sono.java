package dwai.sono;

import dwai.sono.client.Client;
import dwai.sono.connection.packet.Packet1Sound;
import dwai.sono.server.Server;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Sono {

    // Hackathon code
    public static volatile float lastAverage = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        server();
    }

    public static void server() {
        Server server = new Server(38171, 3);
        server.start();

        TargetDataLine microphone = getMicrophone();
        if (microphone == null) {
            System.out.println("Unable to find microphone");
            return;
        }
        byte[] temp = new byte[4000];
        while (true) {
            lastAverage = getAverage(temp, microphone);
        }
    }

    public static void client() {
        Client client = new Client(38171, "172.31.241.43");
        client.start();

        TargetDataLine microphone = getMicrophone();
        if (microphone == null) {
            System.out.println("Unable to find microphone");
            return;
        }
        byte[] temp = new byte[4000];
        while (true) {
            lastAverage = getAverage(temp, microphone);
            client.getConnection().send(new Packet1Sound((float) lastAverage));
        }
    }

    public static float getAverage(byte[] input, TargetDataLine microphone) {
        microphone.read(input, 0, input.length);
        float count = 0;
        for (int i = 0; i < input.length; i++) {
            count += Math.abs(input[i]);
        }
        return count / (input.length * 128);
    }

    public static TargetDataLine getMicrophone() {
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 2, true, true);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            microphone.open(format);
            microphone.start();
            return microphone;
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sono.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
