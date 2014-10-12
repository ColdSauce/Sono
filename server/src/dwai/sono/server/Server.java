package dwai.sono.server;

import dwai.sono.connection.EndPoint;
import dwai.sono.connection.Packet;
import dwai.sono.connection.packet.Packet1Sound;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Server extends EndPoint {

    private final int maxConnections;

    private ExecutorService clientPool;
    private ServerSocket serverSocket;
    private ThreadAccept acceptThread;

    public Server(int port, int maxConnections) {
        super(port, new ServerPacketHandler(), new ArrayBlockingQueue<>(128));
        this.maxConnections = maxConnections;

        Packet.registerPacket(Packet1Sound.class, Packet1Sound.getDecoder());
    }

    @Override
    protected void bind() throws IOException {
        clientPool = Executors.newFixedThreadPool(maxConnections);
        serverSocket = new ServerSocket();
        serverSocket.setPerformancePreferences(1, 2, 0);
        serverSocket.bind(new InetSocketAddress(getPort()), 64);
        acceptThread = new ThreadAccept(serverSocket, this);
        acceptThread.start();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        acceptThread.shutdown();
        clientPool.shutdownNow();
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public ExecutorService getClientPool() {
        return clientPool;
    }
}
