package dwai.sono.server;

import dwai.sono.Sono;
import dwai.sono.connection.Connection;
import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet1Sound;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ServerPacketHandler implements PacketHandler {

    private Connection target = null;

    @Override
    public void handle(Packet packet) {
        if (packet instanceof Packet1Sound) {
            Packet1Sound test = (Packet1Sound) packet;
            System.out.println(bar(test.getAverage() - Sono.lastAverage));
            test.setDifference(test.getAverage() - Sono.lastAverage);
            if (target != null) {
                target.send(test);
            }
        } else if (packet instanceof Packet1Sound) {
            target = packet.getSender();
        }
    }

    public String bar(float amount) {
        int modified = (int) (amount * 100);
        StringBuilder builder = new StringBuilder(100);
        if (modified < 0) {
            for (int i = 0; i < 100; i++) {
                builder.append(i > 100 - Math.abs(modified) ? 'X' : ' ');
            }
            builder.append('|');
        } else {
            for (int i = 0; i < 100; i++) {
                builder.append(' ');
            }
            builder.append('|');
            for (int i = 0; i < Math.abs(modified); i++) {
                builder.append('X');
            }
        }
        return builder.toString();
    }
}
