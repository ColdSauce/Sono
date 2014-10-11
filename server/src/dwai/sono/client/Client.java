package dwai.sono.client;

import dwai.sono.connection.Connection;
import dwai.sono.connection.EndPoint;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
 */
public class Client extends EndPoint {

    private final String hostname;

    private Socket socket;
    private Connection serverConnection;
    private Thread serverThread;

    public Client(int port, String hostname) {
        super(port, new ClientPacketHandler(), new ArrayBlockingQueue<>(128));
        this.hostname = hostname;
    }

    @Override
    protected boolean bind() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, getPort()));
            serverConnection = new Connection(socket, getProcessingQueue());
            serverThread = new Thread(serverConnection);
            serverThread.start();
            return true;
        } catch (Exception e) {
            Logger.getLogger(Client.class.toString()).log(Level.WARNING, null, e);
        }
        return false;
    }
}
