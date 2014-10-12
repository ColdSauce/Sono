package dwai.sono.connection;

import java.io.IOException;
import java.net.BindException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Joseph Cumbo (mooman219)
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

    protected abstract void bind() throws IOException;

    public boolean start() {
        try {
            bind();
            processThread = new ThreadProcess(handler, processingQueue);
            processThread.start();
            return true;
        } catch (BindException ex) {
            System.out.println("Port " + port + " already binded. Unable to start server.");
        } catch (IOException ex) {
            ex.printStackTrace();
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
