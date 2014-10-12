package dwai.sono.server;

import dwai.sono.Sono;
import dwai.sono.connection.Connection;
import dwai.sono.connection.Packet;
import dwai.sono.connection.PacketHandle;
import dwai.sono.connection.PacketHandler;
import dwai.sono.connection.packet.Packet1Sound;
import dwai.sono.connection.packet.Packet2CoolKid;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ServerPacketHandler {

    private static Connection target = null;

    public static void setup(PacketHandler handler) {
        handler.register(Packet1Sound.class, new PacketHandle() {
            @Override
            public void handle(Packet packet) {
                Packet1Sound test = (Packet1Sound) packet;
                System.out.println(bar(test.getAverage() - Sono.lastAverage));
                test.setDifference(test.getAverage() - Sono.lastAverage);
                if (target != null) {
                    target.send(test);
                }
            }
        });
        handler.register(Packet2CoolKid.class, new PacketHandle() {
            @Override
            public void handle(Packet packet) {
                target = packet.getSender();
            }
        });
    }

    public static String bar(float amount) {
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
