package dwai.sono.client;

import dwai.sono.connection.Connection;
import dwai.sono.connection.EndPoint;
import dwai.sono.connection.Packet;
import dwai.sono.connection.packet.Packet1Sound;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Client extends EndPoint {

    private final String hostname;

    private Socket socket;
    private Connection connection;
    private Thread serverThread;

    public Client(int port, String hostname) {
        super(port, new ArrayBlockingQueue<Packet>(128));
        this.hostname = hostname;

        Packet.registerPacket(Packet1Sound.class, Packet1Sound.getDecoder());
    }

    @Override
    protected void bind() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, getPort()));
        connection = new Connection(socket, getProcessingQueue());
        serverThread = new Thread(connection);
        serverThread.start();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        connection.shutdown();
    }

    public Connection getConnection() {
        return connection;
    }

}
