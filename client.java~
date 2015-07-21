
package server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sushil
 */
public class client {
   
   client() throws IOException{
       Socket sock = new Socket("127.0.0.1",5000);
       System.out.println(sock.getInetAddress());       
       Thread read = new Thread(new Runnable(){

           BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
           public void run() {
               while(true){
                   try {
                       String str = input.readLine();
                       System.out.println(str);
                   } catch (IOException ex) {
                       Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
           }
           
       });
       
       Thread send = new Thread(new Runnable(){
          PrintWriter output = new PrintWriter(sock.getOutputStream(), true);
           public void run(){
               Scanner S = new Scanner(System.in);
               System.out.println("Your Name: ");
               String name = S.next();
               output.println(name);
               while(true){
                   if(sock.isClosed())
                   {
                       System.out.println("Server Down");
                       Thread.currentThread().stop();
                   }    
                   String str = S.nextLine();
                   output.println(str);
               }
          } 
       });
       
       send.start();
       read.start();
   } 
    public static void main(String arg[]) throws IOException{
        new client();   
    }
}
