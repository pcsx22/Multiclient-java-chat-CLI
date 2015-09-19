/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import javax.sound.sampled.*;

/**
 *
 * @author sushil
 */
public class AudioHandler {
    TargetDataLine input;
    SourceDataLine output;
    AudioFormat format;
    AudioInputStream audioStream;
    int chunkSize = 4096 * 4;
    byte[] inputData;
    byte[] outputData;
    AudioHandler(){
        format = new AudioFormat(8000.0f, 16, 1, true, true);
    }
    
    void init() throws LineUnavailableException{
        input = AudioSystem.getTargetDataLine(format);
        output = AudioSystem.getSourceDataLine(format);
        audioStream = new AudioInputStream(input);
        inputData = new byte[chunkSize];
        input.open();
        output.open();
    }
    
    void startMic(){
        input.start();
    }
    
    void startSpeaker(){
        output.start();
    }
    
    void closeMic(){
        input.stop();
    }
    
    void closeSpeaker(){
        output.stop();
    }
    
    int getAudio(byte[] data) throws Exception{
        int readCount = audioStream.read(data,0,chunkSize);
        return readCount;
    }
    
    void playAudio(byte[] data){
        output.write(data, 0, chunkSize);
    }
    
}
