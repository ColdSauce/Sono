package dwai.sono;

import dwai.sono.packet.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
 */
public class Connection implements Runnable {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ArrayBlockingQueue<Packet> pendingPackets = new ArrayBlockingQueue<>(128);
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sending is not instant. It is queued to be sent.
     *
     * @param packet Packet to send.
     */
    public void send(Packet packet) {
        try {
            pendingPackets.put(packet);
        } catch (InterruptedException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
                if (in.available() > 0) {
                    Packet packet = Packet.read(in.readByte(), in);
                }
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!pendingPackets.isEmpty()) {
                Packet packet;
                while ((packet = pendingPackets.poll()) != null) {
                    Packet.send(out, packet);
                }
            }
        }
    }

    public void shutdown() {
        running.set(false);
    }
}
