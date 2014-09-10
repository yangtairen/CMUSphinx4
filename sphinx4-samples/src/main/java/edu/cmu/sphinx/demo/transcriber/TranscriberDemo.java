/*
 * Copyright 1999-2013 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

package edu.cmu.sphinx.demo.transcriber;

import java.io.InputStream;
import java.net.URL;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.demo.speakerid.SpeakerIdentificationDemo;
import edu.cmu.sphinx.linguist.acoustic.tiedstate.Sphinx3Loader;
import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.util.TimeFrame;


/**
 * A simple example that shows how to transcribe a continuous audio file that
 * has multiple utterances in it.
 */
public class TranscriberDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("Loading models...");

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/acoustic/wsj");
        
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp");
        
        
        URL url = SpeakerIdentificationDemo.class.getResource("out.wav");
        
        StreamSpeechRecognizer recognizer = 
            new StreamSpeechRecognizer(configuration);
        InputStream stream = url.openStream();
        
        TimeFrame t;
        
        long startTime = 14640;
        long endTime = 14640 + 24830;
        
        t = new TimeFrame(startTime, endTime);
        
        recognizer.startRecognition(stream, t);

        SpeechResult result;

        
        System.out.println("The results with the unadapted model");
        while ((result = recognizer.getResult()) != null) {
        
            System.out.format("Hypothesis: %s\n",
                              result.getHypothesis());
        }

        recognizer.stopRecognition();

        
//        System.out.println("Collect stats. Create Mllr Matrix");
//        stream = url.openStream();
//        recognizer.startRecognition(stream, true);
////        Sphinx3Loader loader = (Sphinx3Loader) recognizer.getLoader();
//        recognizer.stopRecognition();
    	

//        System.out.println("The results with the adapted model");
//        stream = url.openStream();
//        
//        recognizer.startRecognition(stream, false);
//
//        while ((result = recognizer.getResult()) != null) {
//        
//            System.out.format("Hypothesis: %s\n",
//                              result.getHypothesis());
//        }
//
//        recognizer.stopRecognition();
        
    }
}
