package dwai.sono.connection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Team DWAI
 */
public abstract class EndPoint {

    private final int port;
    private final PacketHandler handler;
    private final ArrayBlockingQueue<Packet> processingQueue;

    private Thread processThread;

    public EndPoint(int port, PacketHandler packetHandler, ArrayBlockingQueue<Packet> processingQueue) {
        this.port = port;
        this.handler = packetHandler;
        this.processingQueue = processingQueue;
    }

    protected abstract boolean bind();

    public boolean start() {
        if (bind()) {
            Runnable packetProcessor = new Runnable() {
                @Override
                public void run() {
                    while (true) { // Hackathon Code
                        try {
                            Packet packet = processingQueue.take();
                            handler.handle(packet);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(EndPoint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            processThread = new Thread(packetProcessor);
            processThread.start();
            return true;
        }
        return false;
    }

    public int getPort() {
        return port;
    }

    public PacketHandler getPacketHandler() {
        return handler;
    }

    public ArrayBlockingQueue<Packet> getProcessingQueue() {
        return processingQueue;
    }
}
