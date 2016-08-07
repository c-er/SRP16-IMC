package srp16.imc.server;

import srp16.imc.IMCConstants;
import srp16.imc.IMCMesType;
import srp16.imc.IMCMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by uday on 8/1/16.
 */
public class IMCServer {
    private static Map<String, Queue<IMCMessage>> map; // maps from intended receiver name (String) to queue of messages
    private static HashSet<String> clientNames;
    private static ServerSocket sock;

    private IMCServer() {}

    public static void main(String args[]) {

        try {
            map = new HashMap<>();
            clientNames = new HashSet<>();
            sock = new ServerSocket(IMCConstants.PORT);
            System.out.println("Initialization complete, listening on port " + IMCConstants.PORT);
            while(true) {
                Socket s = sock.accept();
                Runnable r = new IMCServerThread(s);
                Thread t = new Thread(r);
                t.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private static class IMCServerThread implements Runnable {

        private Socket sock;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public IMCServerThread(Socket s) {
            sock = s;
        }

        public void run() {
            System.out.println("Connected to IP: " + sock.getInetAddress());
            try {
                out = new ObjectOutputStream(sock.getOutputStream());
                in = new ObjectInputStream(sock.getInputStream());
                while(true) {
                    IMCMessage mes = (IMCMessage) in.readObject();
                    IMCMessage ret;
                    switch(mes.type) {
                        case REGISTER:
                            if(clientNames.contains(mes.myName)) {
                                ret = new IMCMessage(IMCMesType.FAILURE);
                                ret.info = "There exists a client with that name!";
                                ret.time = new Timestamp(new Date().getTime());
                                out.writeObject(ret);
                                sock.close();
                                return;
                            }
                            clientNames.add(mes.myName);
                            ret = new IMCMessage(IMCMesType.SUCCESS);
                            ret.time = new Timestamp(new Date().getTime());
                            out.writeObject(ret);
                            break;
                        case GET_CLIENTS:
                            if(!clientNames.contains(mes.myName)) {
                                ret = new IMCMessage(IMCMesType.FAILURE);
                                ret.info = "Register yourself fool!";
                                ret.time = new Timestamp(new Date().getTime());
                                out.writeObject(ret);
                                break;
                            }
                            ret = new IMCMessage(IMCMesType.RET_CLIENTS);
                            ret.data = clientNames;
                            ret.time = new Timestamp(new Date().getTime());
                            out.writeObject(ret);
                            break;
                        case SEND_MES_NAME:
                            if(!clientNames.contains(mes.myName)) {
                                ret = new IMCMessage(IMCMesType.FAILURE);
                                ret.info = "Register yourself fool!";
                                ret.time = new Timestamp(new Date().getTime());
                                out.writeObject(ret);
                                break;
                            }
                            if(!clientNames.contains(mes.destName)) {
                                ret = new IMCMessage(IMCMesType.NOT_REGISTERED);
                                ret.info = mes.destName + " is not registered!";
                                ret.time = new Timestamp(new Date().getTime());
                                out.writeObject(ret);
                                break;
                            }
                            if(!map.containsKey(mes.destName)) {
                                Queue<IMCMessage> q = new LinkedList<>();
                                q.offer(mes);
                                map.put(mes.destName, q);
                            } else {
                                map.get(mes.destName).offer(mes);
                            }
                            ret = new IMCMessage(IMCMesType.SUCCESS);
                            ret.time = new Timestamp(new Date().getTime());
                            out.writeObject(ret);
                            break;
                        case GET_MESSAGE:
                            if(!clientNames.contains(mes.myName)) {
                                ret = new IMCMessage(IMCMesType.FAILURE);
                                ret.info = "Register yourself fool!";
                                ret.time = new Timestamp(new Date().getTime());
                                out.writeObject(ret);
                                break;
                            }
                            ret = new IMCMessage(IMCMesType.RET_MESSAGE);

                            if(!map.containsKey(mes.myName)) {
                                ret.data = null;
                            } else {
                                ret.data = map.get(mes.myName).poll();
                            }
                            ret.time = new Timestamp(new Date().getTime());
                            out.writeObject(ret);
                            break;

                    }
                }

            } catch (EOFException e) {
                System.out.println("Disconnected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}