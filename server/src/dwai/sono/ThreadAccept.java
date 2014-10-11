package dwai.sono;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
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
                Connection connection = new Connection(socket);
            } catch (Exception e) {
                Logger.getLogger(ThreadAccept.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }

    public void shutdown() {
        running.set(false);
    }
}
