package dwai.sono.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Joseph Cumbo (mooman219)
 */
public class Connection implements Runnable {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ArrayBlockingQueue<Packet> pendingPackets = new ArrayBlockingQueue<>(128);
    private final ArrayBlockingQueue<Packet> processingQueue;
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Connection(Socket socket, ArrayBlockingQueue<Packet> processingQueue) throws IOException {
        this.socket = socket;
        this.processingQueue = processingQueue;
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
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            attemptReads();
            if (!attemptWrites()) {
                System.out.println("Connection error, shutting down connection.");
                shutdown();
            }
        }
        try {
            this.socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Connection ended");
    }

    private boolean attemptReads() {
        try {
            while (in.available() > 0) {
                Packet packet = Packet.read(in.readByte(), in);
                packet.setSender(this);
                processingQueue.offer(packet);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private boolean attemptWrites() {
        if (!pendingPackets.isEmpty()) {
            Packet packet;
            while ((packet = pendingPackets.poll()) != null) {
                if (!Packet.send(out, packet)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shutdown() {
        running.set(false);
    }
}
