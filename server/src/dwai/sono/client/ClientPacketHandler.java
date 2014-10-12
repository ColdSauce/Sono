package dwai.sono.client;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet1Test;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ClientPacketHandler implements PacketHandler {

    @Override
    public void handle(Packet packet) {
        if (packet instanceof Packet1Test) {
            Packet1Test test = (Packet1Test) packet;
            System.out.println(test.getTestInt() + " C " + test.getTestShort());
        }
    }
}
