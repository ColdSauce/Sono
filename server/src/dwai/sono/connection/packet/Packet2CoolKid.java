package dwai.sono.connection.packet;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketDecoder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Packet2CoolKid extends Packet {

    public Packet2CoolKid() {
    }

    @Override
    protected void write(ObjectOutputStream out) throws IOException {
    }

    @Override
    protected void read(ObjectInputStream in) throws IOException {
    }

    public static PacketDecoder getDecoder() {
        return new PacketDecoder() {
            @Override
            public Packet decode(ObjectInputStream in) throws IOException {
                return new Packet2CoolKid();
            }
        };
    }
}
