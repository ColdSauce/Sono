package dwai.sono.client;

import dwai.sono.connection.Connection;
import dwai.sono.server.ThreadAccept;
import dwai.sono.connection.Packet;
import dwai.sono.server.ServerPacketHandler;
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
public class Client {

    private final int port;
    private final int maxConnections;
    private final ServerPacketHandler handler;
    private final ArrayBlockingQueue<Packet> processingQueue;

    private ExecutorService clientPool;
    private Connection serverConnection;
    private Thread serverThread;

    public Client(int port, int maxConnections) {
        this.port = port;
        this.maxConnections = maxConnections;
        this.processingQueue = new ArrayBlockingQueue<>(128);
        this.handler = new ServerPacketHandler();
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
                Packet packet = processingQueue.take();
                handler.handle(packet);
            }
        } catch (Exception e) {
            Logger.getLogger(Client.class.toString()).log(Level.WARNING, null, e);
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

    public ArrayBlockingQueue<Packet> getProcessingQueue() {
        return processingQueue;
    }
}
