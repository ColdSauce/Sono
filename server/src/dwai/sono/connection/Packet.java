package dwai.sono.connection;

import dwai.sono.connection.PacketDecoder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
 */
public abstract class Packet {

    private static final AtomicInteger packetCount = new AtomicInteger(1);

    private static final HashMap<Class<? extends Packet>, Byte> ids = new HashMap<Class<? extends Packet>, Byte>();
    private static final HashMap<Byte, PacketDecoder> decoders = new HashMap<Byte, PacketDecoder>();

    public static Packet read(byte id, ObjectInputStream in) {
        PacketDecoder decoder = decoders.get(id);
        if (decoder != null) {
            try {
                return decoder.decode(in);
            } catch (IOException ex) {
                Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static void send(ObjectOutputStream out, Packet p) {
        byte id = ids.get(p.getClass());
        if (id != 0) {
            try {
                out.writeByte(id);
                p.write(out);
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new IllegalStateException("Packet.send: Packet ID (" + id + ") does not exist.");
        }
    }

    public static void registerPacket(Class<? extends Packet> packet, PacketDecoder decoder) {
        byte id = (byte) (packetCount.getAndIncrement() & 0xFF);
        if (decoders.containsKey(id)) {
            throw new IllegalStateException("Too many packets have been registered. Unable to register " + id);
        } else {
            decoders.put(id, decoder);
            ids.put(packet, id);
        }
    }

    protected abstract void write(ObjectOutputStream out) throws IOException;

    protected abstract void read(ObjectInputStream in) throws IOException;

}
