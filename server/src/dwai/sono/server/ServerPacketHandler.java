package dwai.sono.server;

import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet1Test;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ServerPacketHandler implements PacketHandler {

    @Override
    public void handle(Packet packet) {
        if (packet instanceof Packet1Test) {
            Packet1Test test = (Packet1Test) packet;
            System.out.println(test.getTestInt() + " S " + test.getTestShort());
            test.setTestInt(0);
            test.setTestShort((short) 0);
            if (test.getSender() != null) {
                test.getSender().send(test);
            }
        }
    }
}
