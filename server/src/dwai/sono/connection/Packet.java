package dwai.sono.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joseph Cumbo (mooman219)
 */
public abstract class Packet {

    private static final AtomicInteger packetCount = new AtomicInteger(1);

    private static final HashMap<Class<? extends Packet>, Byte> ids = new HashMap<Class<? extends Packet>, Byte>();
    private static final HashMap<Byte, PacketDecoder> decoders = new HashMap<Byte, PacketDecoder>();

    private Connection sender = null;

    public static byte getPacketId(Class<? extends Packet> packet) {
        return ids.get(packet);
    }

    public static Packet read(byte id, ObjectInputStream in) {
        PacketDecoder decoder = decoders.get(id);
        if (decoder != null) {
            try {
                return decoder.decode(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Writes Packet 'p' to the output 'out'.
     *
     * @param out the output being written to.
     * @param p the packet being written.
     * @throws IllegalStateException if the packet being written has not been
     * registered.
     * @return false if the underlying socket has closed, true otherwise.
     */
    public static boolean send(ObjectOutputStream out, Packet p) {
        byte id = ids.get(p.getClass());
        if (id != 0) {
            try {
                out.writeByte(id);
                p.write(out);
                out.flush();
            } catch (SocketException ex) {
                return false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Packet.send: Packet ID (" + id + ") does not exist.");
        }
        return true;
    }

    public static void registerPacket(Class<? extends Packet> packet, PacketDecoder decoder) {
        if (ids.containsKey(packet)) {
            System.out.println("Packet " + packet.getName() + " already registered");
            return;
        }
        byte id = (byte) (packetCount.getAndIncrement() & 0xFF);
        if (decoders.containsKey(id)) {
            throw new IllegalStateException("Too many packets have been registered. Unable to register " + id);
        } else {
            decoders.put(id, decoder);
            ids.put(packet, id);
        }
    }

    public Connection getSender() {
        return sender;
    }

    public void setSender(Connection sender) {
        this.sender = sender;
    }

    protected abstract void write(ObjectOutputStream out) throws IOException;

    protected abstract void read(ObjectInputStream in) throws IOException;

}
