package dwai.sono.server;

import dwai.sono.connection.EndPoint;
import java.io.IOException;
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
public class Server extends EndPoint {

    private final int maxConnections;

    private ExecutorService clientPool;
    private ServerSocket serverSocket;
    private ThreadAccept acceptThread;

    public Server(int port, int maxConnections) {
        super(port, new ServerPacketHandler(), new ArrayBlockingQueue<>(128));
        this.maxConnections = maxConnections;
    }

    @Override
    protected boolean bind() {
        try {
            clientPool = Executors.newFixedThreadPool(maxConnections);
            serverSocket = new ServerSocket();
            serverSocket.setPerformancePreferences(1, 2, 0);
            serverSocket.bind(new InetSocketAddress(getPort()), 64);
            acceptThread = new ThreadAccept(serverSocket, this);
            acceptThread.start();
            return true;
        } catch (IOException e) {
            Logger.getLogger(Server.class.toString()).log(Level.WARNING, null, e);
        }
        return false;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public ExecutorService getClientPool() {
        return clientPool;
    }
}
