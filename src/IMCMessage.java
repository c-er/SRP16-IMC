import java.io.Serializable;

/**
 * Created by uday on 8/1/16.
 */
public class IMCMessage implements Serializable {
    public int sender;
    public int receiver;
    public Object data;

    public IMCMessage(int sender, int receiver, Object data) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
    }

    @Override
    public String toString() {
        return "IMCMessage{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", data=" + data +
                '}';
    }
}
