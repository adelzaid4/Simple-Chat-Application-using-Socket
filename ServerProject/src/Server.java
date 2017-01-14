
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Adel
 */
public class Server {

    ServerSocket serSocket;

    public Server() {
        try {
            serSocket = new ServerSocket(5005);
            while (true) {
                Socket s;
                s = serSocket.accept();
                new ChatHandler(s);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Server();
    }
}

class ChatHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    static ArrayList<ChatHandler> clientsList = new ArrayList<ChatHandler>();

    public ChatHandler(Socket cs) {
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            clientsList.add(this);
            start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void run() {
        while (true) {
            String str;
            try {
                str = dis.readLine();
                if (!str.equals(" ")) {
                    sendMessageToAll(str);
                }

            } catch (SocketException ex) {
                try {
                    dis.close();
                    ps.close();
                    clientsList.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void sendMessageToAll(String str) {
        for (ChatHandler ch : clientsList) {
            ch.ps.println(str);
        }

    }

}
