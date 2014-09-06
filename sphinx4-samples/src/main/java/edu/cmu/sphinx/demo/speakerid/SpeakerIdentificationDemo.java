package edu.cmu.sphinx.demo.speakerid;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.speakerid.Segment;
import edu.cmu.sphinx.speakerid.SpeakerCluster;
import edu.cmu.sphinx.speakerid.SpeakerIdentification;
import edu.cmu.sphinx.util.TimeFrame;


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
    public static void printSpeakerIntervals(ArrayList<SpeakerCluster> speakers)
            throws IOException {
        int idx = 0;
        for (SpeakerCluster spk : speakers) {
            ArrayList<Segment> segments = spk.getArrayOfSegments();
            for (Segment seg : segments)
                System.out.println(seg.getStartTime() + " "
                        + seg.getLength() + " Speaker" + (++idx));
        }        
    }

    public static void main(String[] args) throws Exception {
    	
	  Configuration configuration = new Configuration();

      configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/acoustic/wsj");
      configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d");
      configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp");
      
    	
        SpeakerIdentification sd = new SpeakerIdentification();
    
        URL url = SpeakerIdentificationDemo.class.getResource("out.wav");
        ArrayList<SpeakerCluster> clusters = sd.cluster(url.openStream());
        
//        printSpeakerIntervals(clusters);
        
      StreamSpeechRecognizer recognizer = 
      new StreamSpeechRecognizer(configuration);

      recognizer.startRecognition(url, true);
      
      SpeechResult result;

  	
//      while ((result = recognizer.getResult()) != null) {
//          System.out.format("Hypothesis: %s\n", result.getHypothesis());
//      }
      
      recognizer.stopRecognition();

    }
}
