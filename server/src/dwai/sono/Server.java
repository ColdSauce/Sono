package dwai.sono;

import dwai.sono.packet.Packet;
import dwai.sono.packet.PacketHandler;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
 */
public class Server {

    private final int port;
    private final int maxConnections;

    private ArrayBlockingQueue<Packet> pendingPackets = new ArrayBlockingQueue<>(128);
    private ExecutorService clientPool;
    private ServerSocket serverSocket;
    private ThreadAccept acceptThread;
    private PacketHandler handler;

    public Server(int port, int maxConnections) {
        this.port = port;
        this.maxConnections = maxConnections;
        this.handler = handler;
    }

    public void start() {
        try {
            clientPool = Executors.newFixedThreadPool(maxConnections);
            serverSocket = new ServerSocket();
            serverSocket.setPerformancePreferences(1, 2, 0);
            serverSocket.bind(new InetSocketAddress(port), 64);
            acceptThread = new ThreadAccept(serverSocket, this);
            acceptThread.start();

            while (true) { // Hackathon Code
                Packet packet = pendingPackets.take();
                handler.handle(packet);
            }
        } catch (Exception e) {
            Logger.getLogger(Server.class.toString()).log(Level.WARNING, null, e);
        }
    }

    public int getPort() {
        return port;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public ExecutorService getClientPool() {
        return clientPool;
    }
}
