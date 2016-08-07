package srp16.imc.client;

import srp16.imc.IMCConstants;
import srp16.imc.IMCMesType;
import srp16.imc.IMCMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by uday on 8/1/16.
 */
public class IMCClient {
    private String name;
    private Socket con;
    private String server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public IMCClient(String name) {
        server = "localhost";
        this.name = name;
    }

    public IMCClient(String host, String name) {
        server = host;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean connect() throws IOException, ClassNotFoundException {
        con = new Socket(server, IMCConstants.PORT);
        out = new ObjectOutputStream(con.getOutputStream());
        in = new ObjectInputStream(con.getInputStream());
        IMCMessage mes = new IMCMessage(IMCMesType.REGISTER);
        mes.myName = name;
        mes.time = new Timestamp(new Date().getTime());
        out.writeObject(mes);
        IMCMessage ret = (IMCMessage) in.readObject();
        if(ret.type != IMCMesType.SUCCESS) {
            return false;
        }
        return true;
    }

    public void disconnect() throws IOException {
        con.close();
        out = null;
        in = null;
    }

    public HashSet<String> getClients() throws IOException, ClassNotFoundException {
        IMCMessage mes = new IMCMessage(IMCMesType.GET_CLIENTS);
        mes.myName = name;
        mes.time = new Timestamp(new Date().getTime());
        out.writeObject(mes);
        IMCMessage ret = (IMCMessage) in.readObject();
        if(ret.type != IMCMesType.RET_CLIENTS) {
            throw new IOException("Client getting failed.");
        }
        return (HashSet<String>) ret.data;
    }

    public boolean sendMessage(Serializable data, String target) throws IOException, ClassNotFoundException {
        IMCMessage mes = new IMCMessage(IMCMesType.SEND_MES_NAME);
        mes.destName = target;
        mes.myName = name;
        mes.data = data;
        mes.time = new Timestamp(new Date().getTime());
        out.writeObject(mes);
        IMCMessage ret = (IMCMessage) in.readObject();
        if(ret.type == IMCMesType.SUCCESS) {
            return true;
        } else if(ret.type == IMCMesType.NOT_REGISTERED) {
            return false;
        } else {
            throw new IOException("Message send not properly processed.");
        }
    }

    public IMCMessage getMessage() throws IOException, ClassNotFoundException {
        IMCMessage mes = new IMCMessage(IMCMesType.GET_MESSAGE);
        mes.myName = name;
        mes.time = new Timestamp(new Date().getTime());
        out.writeObject(mes);
        IMCMessage ret = (IMCMessage) in.readObject();
        if(ret.type != IMCMesType.RET_MESSAGE) {
            throw new IOException("Could not get message");
        }
        return ret;
    }
}