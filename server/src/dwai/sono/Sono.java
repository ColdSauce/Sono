package dwai.sono;

import dwai.sono.client.Client;
import dwai.sono.connection.packet.Packet1Test;
import dwai.sono.server.Server;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Sono {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server(38171, 5);
        Client client = new Client(38171, "localhost");
        Client client2 = new Client(38171, "localhost");

        server.start();
        client.start();

        Packet1Test test = new Packet1Test();
        test.setTestInt(1);
        test.setTestShort((short) 1);
        client.getConnection().send(test);

        client.shutdown();
    }

}
