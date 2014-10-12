package dwai.sono.server;

import dwai.sono.connection.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class ThreadAccept extends Thread {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ServerSocket serverSocket;
    private final Server server;

    public ThreadAccept(ServerSocket serverSocket, Server server) {
        super("Sono Accept");
        this.serverSocket = serverSocket;
        this.server = server;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, server.getProcessingQueue());
                server.getClientPool().submit(connection);
            } catch (IOException ex) {
                System.out.println("Error accepting connection. Skipping.");
                ex.printStackTrace();
            }
        }
    }

    public void shutdown() {
        running.set(false);
    }
}
