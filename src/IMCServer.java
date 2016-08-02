import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by uday on 8/1/16.
 */
public class IMCServer {

    static HashMap<Integer, Queue<IMCMessage>> qs = new HashMap<>();

    public static void main(String args[]) throws Exception {
        ServerSocket ssock = new ServerSocket(8888);
        while(true) {
            Socket sock = ssock.accept();
            Runnable t = new Thing(sock, qs);
            new Thread(t).start();
        }
    }
}

class Thing implements Runnable {

    Socket sock;
    HashMap<Integer, Queue<IMCMessage>> qs;

    public Thing(Socket s, HashMap<Integer, Queue<IMCMessage>> q) {
        sock = s;
        this.qs = q;
    }

    public void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(sock.getInputStream());
            out = new ObjectOutputStream(sock.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                IMCMessage s = (IMCMessage) in.readObject();
                if(s.sender < 0) {
                    out.writeObject(qs.get(s.receiver).poll());
                } else {
                    if(!qs.containsKey(s.receiver)) {
                        Queue<IMCMessage> l = new LinkedList<>();
                        l.add(s);
                        qs.put(s.receiver, l);
                    } else {
                        qs.get(s.receiver).add(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}