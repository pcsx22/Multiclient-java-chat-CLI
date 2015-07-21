/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

/**
 *
 * @author sushil
 */
public class Message implements java.io.Serializable{
    Message(String src,String t,String msg){
        this.src = src;
        target = t;
        text = msg;
        
    }
    String src;
    String target;
    String text;
}

class FileMessage extends Message{
    FileMessage(String src,String t,String msg,String path){
        super(src,t,msg);
        this.path = path;
    }
    String path;
}

class TextMessage extends Message{
    TextMessage(String src,String t,String msg){
        super(src,t,msg);
    }
    
}

class OnlineStat extends Message{ //command for viewing the online users
    OnlineStat(String src){
        super(src,src,null);
    }
    
}
