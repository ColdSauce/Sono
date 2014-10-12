package dwai.sono.server;

import dwai.sono.Sono;
import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet1Sound;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ServerPacketHandler implements PacketHandler {

    @Override
    public void handle(Packet packet) {
        if (packet instanceof Packet1Sound) {
            Packet1Sound test = (Packet1Sound) packet;
            System.out.println(Sono.lastAverage - test.getAverage());
        }
    }
}
