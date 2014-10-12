package dwai.sono.connection;

import java.util.HashMap;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class PacketHandler {

    private HashMap<Class<? extends Packet>, PacketHandle> handlers = new HashMap<Class<? extends Packet>, PacketHandle>();

    public void register(Class<? extends Packet> packet, PacketHandle handle) {
        handlers.put(packet, handle);
    }

    public void handle(Packet packet) {
        handlers.get(packet.getClass()).handle(packet);
    }
}
