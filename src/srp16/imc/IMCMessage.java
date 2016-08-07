package srp16.imc;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by uday on 8/1/16.
 */
public class IMCMessage implements Serializable {
    public IMCMesType type;
    public String myName;
    public String destName;
    public Serializable data;
    public String info;
    public Timestamp time;

    public IMCMessage(IMCMesType t) {
        type = t;
        info = "";
        data = null;
        destName = "";
        myName = "";
    }

    @Override
    public String toString() {
        return "IMCMessage{" +
                "type=" + type +
                ", myName='" + myName + '\'' +
                ", destName='" + destName + '\'' +
                ", data=" + data +
                ", info='" + info + '\'' +
                ", time=" + time +
                '}';
    }
}
