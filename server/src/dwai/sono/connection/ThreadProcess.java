package dwai.sono.connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
 */
public class ThreadProcess extends Thread {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final PacketHandler packetHandler;
    private final ArrayBlockingQueue<Packet> processingQueue;

    public ThreadProcess(PacketHandler packetHandler, ArrayBlockingQueue<Packet> processingQueue) {
        super("Sono Process");
        this.packetHandler = packetHandler;
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
                Packet packet = processingQueue.take();
                packetHandler.handle(packet);
            } catch (InterruptedException ex) {
                Logger.getLogger(EndPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void shutdown() {
        running.set(false);
    }
}
