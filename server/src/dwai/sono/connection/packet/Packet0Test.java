package dwai.sono.connection.packet;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketDecoder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Joseph Cumbo
 */
public class Packet0Test extends Packet {

    static {
        Packet.registerPacket(Packet0Test.class, new PacketDecoder() {
            @Override
            public Packet decode(ObjectInputStream in) throws IOException {
                Packet0Test packet = new Packet0Test();
                packet.read(in);
                return packet;
            }
        });
    }

    private int testInt = 0;
    private short testShort = 0;

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public short getTestShort() {
        return testShort;
    }

    public void setTestShort(short testShort) {
        this.testShort = testShort;
    }

    @Override
    protected void write(ObjectOutputStream out) throws IOException {
        out.writeInt(testInt);
        out.writeShort(testShort);
    }

    @Override
    protected void read(ObjectInputStream in) throws IOException {
        testInt = in.readInt();
        testShort = in.readShort();
    }
}
