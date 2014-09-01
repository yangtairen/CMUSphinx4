package edu.cmu.sphinx.demo.speakerid;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;

import edu.cmu.sphinx.demo.transcriber.TranscriberDemo;
import edu.cmu.sphinx.speakerid.Segment;
import edu.cmu.sphinx.speakerid.SpeakerCluster;
import edu.cmu.sphinx.speakerid.SpeakerIdentification;
import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.util.props.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SpeakerIdentificationDemo {

    /**
     * Returns string version of the given time in miliseconds 
     * @param seconds
     * @return time in format mm:ss
     */
    public static String time(int seconds) {
        return (seconds / 60000) + ":" + (Math.round((double) (seconds % 60000) / 1000));
    }

    /**
     * 
     * @param speakers
     *            An array of clusters for which it is needed to be printed the
     *            speakers intervals
     * @throws IOException
     */
    public static void printSpeakerIntervals(ArrayList<SpeakerCluster> speakers, String fileName)
            throws IOException {
        int idx = 0;
        for (SpeakerCluster spk : speakers) {
            idx++;
            ArrayList<Segment> segments = spk.getArrayOfSegments();
            for (Segment seg : segments)
                System.out.println(time(seg.getStartTime()) + " "
                        + time(seg.getLength()) + " Speaker" + idx);
        }
//        
//        ArrayList<Segment> s = speakers.get(0).getSpeakerIntervals();
//        ArrayList<Segment> s2 = speakers.get(0).getArrayOfSegments();
        
        
//        System.out.println("speaker intervals");
//        for(Segment seg : s)
//        	System.out.println(time(seg.getStartTime()) + " " + time(seg.getLength()) + " Speaker 1");
        
//        System.out.println("array of segments");
//        for(Segment seg : s2)
//        	System.out.println(time(seg.getStartTime()) + " " + time(seg.getLength()) + " Speaker 1");
        
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
    	
        SpeakerIdentification sd = new SpeakerIdentification();
    
        URL url = SpeakerIdentificationDemo.class.getResource("JamesCameron_2010_310.25_329.97.wav");
        
       
//       TargetDataLine line = null;
//       InputStream target1 = url.openStream();
//       AudioInputStream target2 = new AudioInputStream(line);
//       InputStream target3 = target2;
       
        // the AudioInputStream needs a TargetDataLine
//        AudioInputStream a = new AudioInputStream((TargetDataLine) url.openStream());
        
        // get the clusters from an InputStream
       
       
//       URL url2 = ClassLoader.getResource("/sounds/file.wav");
       AudioInputStream ais = AudioSystem.getAudioInputStream(url);
       Clip clip = AudioSystem.getClip();
       
       
       System.out.println("cool");
       
//        ArrayList<SpeakerCluster> clusters = sd.cluster(target1);
        
//        printSpeakerIntervals(clusters, url.getPath());
    
//        clusters.get(0).
        
        
        
        
        
        
        
        
        
    }
}
