package dwai.sono;

import dwai.sono.client.Client;
import dwai.sono.connection.packet.Packet0Test;
import dwai.sono.server.Server;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server
 *
 * @author Team DWAI
 */
public class Sono {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server(38171, 5);
        Client client = new Client(38171, "localhost");

        server.start();
        client.start();

        Packet0Test test = new Packet0Test();
        test.setTestInt(21);
        test.setTestShort((short) 2);
        client.getConnection().send(test);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sono.class.getName()).log(Level.SEVERE, null, ex);
        }

        test.setTestInt(43);
        test.setTestShort((short) 43);
        client.getConnection().send(test);
    }

}
