package dwai.sono.connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Joseph Cumbo (mooman219)
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
                ex.printStackTrace();
            }
        }
    }

    public void shutdown() {
        running.set(false);
    }
}
