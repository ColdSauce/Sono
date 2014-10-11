package dwai.sono.client;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet0Test;

/**
 * @author Team DWAI
 */
public class ClientPacketHandler implements PacketHandler {

    @Override
    public void handle(Packet packet) {
        if (packet instanceof Packet0Test) {
            Packet0Test test = (Packet0Test) packet;
            System.out.println(test.getTestInt() + " C " + test.getTestShort());
        }
    }
}
