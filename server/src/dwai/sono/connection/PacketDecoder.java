package dwai.sono.connection;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author Joseph Cumbo (mooman219)
 */
public interface PacketDecoder {

    public Packet decode(ObjectInputStream in) throws IOException;

}
