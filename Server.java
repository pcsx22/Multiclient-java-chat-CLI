/*
Now I need to support the multi-client chat
for that I need to create a list of threads i.e users(addd a field client name to identify the client)
this list of threads and little bit of synchronization will do the work.
*/

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sushil
 */
class Colln{
static ArrayList<PrintWriter> writerColln = new ArrayList();
static ArrayList<SocThread> threads = new ArrayList();
}

class SocThread extends Thread{
    public String clientName;
    Socket sock;
    PrintWriter P;
    BufferedReader input;
    SocThread(Socket Soc){
        sock = Soc;
    }
    
    public void run(){
        try {
           // System.out.println("hello");
            P = new PrintWriter(sock.getOutputStream(),true);
            Colln.writerColln.add(P);
            input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            clientName = input.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SocThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        P.println(clientName + " You are connected to the server :)");
        while(true){
            
            try {
                String msg = input.readLine();
                if(msg == null){
                    System.out.println("Connection Closed");
                    Thread.currentThread().stop();
                }
                System.out.println("Message Received: " + msg +" From: " + clientName + "\n");
                
                if(msg.startsWith("@")){
                    int lastIndex = msg.indexOf(" ");
                    String name = msg.substring(1, lastIndex);
                    System.out.println("Hello" + name);
                    for(int i = 0;i<Colln.writerColln.size();i++){
                       if(name != null && name.equals(Colln.threads.get(i).clientName)){
                            Colln.threads.get(i).P.println( this.clientName + ":" + msg.substring(lastIndex,msg.length()));
                        }
                    }
                  }
                else{   
                for(int i = 0;i<Colln.writerColln.size();i++){
                    Colln.writerColln.get(i).println(msg);
                    }
                }
            } catch (IOException ex) {
                    Logger.getLogger(SocThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //System.out.println(sock.getInetAddress());
        
    }
}

public class Server {

    /**
     * @param args the command line arguments
     */
    Server() throws IOException{
        ServerSocket sSocket = new ServerSocket(5000);
        System.out.println("Started at" + new Date());
        while(true){
          Socket sock = sSocket.accept();
          SocThread clientThread = new SocThread(sock);
          clientThread.start();
          Colln.threads.add(clientThread);
        }
    }
    
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        new Server();
    }
    
}
