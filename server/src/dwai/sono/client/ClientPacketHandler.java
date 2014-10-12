package dwai.sono.client;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet1Sound;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ClientPacketHandler implements PacketHandler {

    @Override
    public void handle(Packet packet) {
        if (packet instanceof Packet1Sound) {
            Packet1Sound test = (Packet1Sound) packet;
            // test.getDifference() Do stuff with
        }
    }
}
