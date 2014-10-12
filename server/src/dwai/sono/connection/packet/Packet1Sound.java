package dwai.sono.connection.packet;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketDecoder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Packet1Sound extends Packet {

    static {
        Packet.registerPacket(Packet1Sound.class, new PacketDecoder() {
            @Override
            public Packet decode(ObjectInputStream in) throws IOException {
                Packet1Sound packet = new Packet1Sound(0);
                packet.read(in);
                return packet;
            }
        });
    }

    private float difference;

    public Packet1Sound(float difference) {
        this.difference = difference;
    }

    public float getAverage() {
        return difference;
    }

    public void setDifference(float difference) {
        this.difference = difference;
    }

    @Override
    protected void write(ObjectOutputStream out) throws IOException {
        out.writeFloat(difference);
    }

    @Override
    protected void read(ObjectInputStream in) throws IOException {
        difference = in.readFloat();
    }
}
