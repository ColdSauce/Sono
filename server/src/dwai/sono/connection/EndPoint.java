package dwai.sono.connection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Team DWAI
 */
public abstract class EndPoint {

    private final int port;
    private final PacketHandler handler;
    private final ArrayBlockingQueue<Packet> processingQueue;

    private ThreadProcess processThread;

    public EndPoint(int port, PacketHandler handler, ArrayBlockingQueue<Packet> processingQueue) {
        this.port = port;
        this.handler = handler;
        this.processingQueue = processingQueue;
    }

    protected abstract boolean bind();

    public boolean start() {
        if (bind()) {
            processThread = new ThreadProcess(handler, processingQueue);
            processThread.start();
            return true;
        }
        return false;
    }

    public void shutdown() {
        processThread.shutdown();
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
