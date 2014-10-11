package dwai.sono.connection;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author Team DWAI
 */
public interface PacketDecoder {

    public Packet decode(ObjectInputStream in) throws IOException;

}
