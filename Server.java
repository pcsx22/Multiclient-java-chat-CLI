package chatapp;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;

/**
 *
 * @author sushil
 */

class Colln{
    static ArrayList<PrintWriter> writerColln = new ArrayList();
    static ArrayList<ServerThread> threads = new ArrayList();
}

class ServerThread extends Thread{
    Socket socket;
    String clientName;
    ObjectInputStream input;
    ObjectOutputStream output;
    Message nMsg;
    Message msg = null;
    ServerThread(Socket soc){
        socket = soc;
    }
   String getAllUsername(String username){
       String R = "";
       for(ServerThread S: Colln.threads){
           if(S.clientName.equals(username))
               continue;
           R += S.clientName;
           R += "\n";
       }
       return R;
   }
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());         
            nMsg = (Message) input.readObject();
        } catch (Exception ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientName = nMsg.src;
        System.out.println(clientName + " is connected");
        while(socket != null){
            try {
                msg = (Message)input.readObject();
                //System.out.println(msg.text);
                for(ServerThread S: Colln.threads){
                    if(S.clientName.equals(msg.target)){
                       if(msg instanceof OnlineStat){
                           msg.text = getAllUsername(msg.src);
                       }
                        S.output.writeObject(msg);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Connection Closed");
                socket = null;
                Colln.threads.remove(this);
                System.out.println(this.clientName + " got disconnected");
//Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
}

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Started at" + new Date());
        while(true){
            Socket socket = ss.accept();
            ServerThread st = new ServerThread(socket);
            Colln.threads.add(st);
            st.start();
        }
    }
}
