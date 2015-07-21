/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author sushil
 */
public class Client {

    String clientName = null;
    Socket soc;
    ObjectOutputStream output;
    ObjectInputStream input;
    Client() throws IOException{
       soc = new Socket("127.0.0.1",5000);
       output = new ObjectOutputStream(soc.getOutputStream());
       input = new ObjectInputStream(soc.getInputStream());
       
    Thread sendThread = new Thread(new Runnable(){
        Scanner S = new Scanner(System.in);
        public void run(){
            while(true){
            if(clientName == null){
                System.out.println("Your name: ");
                clientName = S.next();
                try {
                    output.writeObject(new TextMessage(clientName,null,null));
                } catch (IOException ex) {
                    ex.printStackTrace();
//Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String msg = S.nextLine();
            try {
                if(msg.startsWith("@")){
                    int lastIndex = msg.indexOf(" ");
                    output.writeObject(new TextMessage(clientName,msg.substring(1,lastIndex),msg.substring(lastIndex, msg.length())));
                }
                else if(msg.startsWith("upload")){
                    new Thread(new Runnable(){
                        int lastIndex = msg.lastIndexOf(" ");
                        String target = msg.substring(8, lastIndex);
                        BufferedReader reader;
                        public void run(){
                            try {
                                reader = new BufferedReader(new FileReader(msg.substring(lastIndex+1, msg.length())));
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String data;
                            try {
                                while((data = reader.readLine())!=null){
                                    output.writeObject(new FileMessage(clientName,target,data,msg.substring(lastIndex+1, msg.length())));
                                }
                                output.writeObject(new FileMessage(clientName,target,null,null));
                                System.out.println("Upload Complete");
                            } catch (IOException ex) {
                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }).start();
                }
                else if(msg.startsWith("online")){
                    output.writeObject(new OnlineStat(clientName));
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(soc.isClosed()){
                System.exit(0);
            }
        }
        }
    });
    sendThread.start();
    
    Thread readThread = new Thread(new Runnable(){
       TextMessage tMsg = null;
       BufferedWriter os = null;
       FileMessage fMsg = null;
       Message msg = null;
       public void run(){
           while(true){
               try {
                   msg = (Message) input.readObject();
                   if(msg instanceof TextMessage)
                       System.out.println(msg.src + ": " + msg.text);
                   else if(msg instanceof FileMessage){
                       if(os == null){
                           fMsg = (FileMessage) msg;
                           int lastIndex = fMsg.path.lastIndexOf("/");
                           os = new BufferedWriter(new FileWriter("/home/sushil/Desktop/A/" + fMsg.path.substring(lastIndex, fMsg.path.length())));
                           System.out.println("Sarting Download..");
                       }
                       
                       if(msg.text == null){
                            os.close();
                            os = null;
                            System.out.println("Download Complete");
                       }
                       else{
                            os.write(msg.text);
                            os.write("\n");
                       }
                   }
                   else if(msg instanceof OnlineStat){
                       System.out.println(msg.text);
                   }
               } catch (IOException ex) {
                   Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
               }
               
           }
       } 
    });
    
    readThread.start();
    }
    public static void main(String[] args) throws Exception{
        new Client();
    }
    
    
}
