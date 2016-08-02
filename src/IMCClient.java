import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by uday on 8/1/16.
 */
public class IMCClient {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Socket sock = new Socket("localhost", 8888);
        System.out.println("Enter ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.println("Enter commands (g gets, s sends): ");
        ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
        while(true) {
            String s = sc.nextLine();
            StringTokenizer st = new StringTokenizer(s);
            if(st.nextToken().equalsIgnoreCase("g")) {
                out.writeObject(new IMCMessage(-1, id, null));
                IMCMessage last = (IMCMessage)in.readObject();
                System.out.println(last);
            } else {
                String data = st.nextToken();
                out.writeObject(new IMCMessage(1, id, data));
            }
        }
    }
}
