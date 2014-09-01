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

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;


/**
 * A simple example that shows how to transcribe a continuous audio file that
 * has multiple utterances in it.
 */
public class TranscriberDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("Loading models...");

        Configuration configuration = new Configuration();

        // Load model from the jar
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/acoustic/wsj");
        
        // You can also load model from folder
        // configuration.setAcousticModelPath("file:en-us");
        
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp");
        
        // If you want to use your own mllr_matrix
//         configuration.setAdaptationFile("/home/gia/Work/mllr_matrix");

        StreamSpeechRecognizer recognizer = 
            new StreamSpeechRecognizer(configuration);
        InputStream stream = TranscriberDemo.class.getResourceAsStream(
                "/edu/cmu/sphinx/demo/MllrDecodingConfig/JamesCameron_2010_310.25_329.97.wav");
        
        // If you want to use your own mllr_matrix
        // recognizer.startRecognition(stream)
        recognizer.startRecognition(stream);

        SpeechResult result;

        
        System.out.println("The results with the unadapted model");
        while ((result = recognizer.getResult()) != null) {
        
            System.out.format("Hypothesis: %s\n",
                              result.getHypothesis());
                              
            System.out.println("List of recognized words and their times:");
            for (WordResult r : result.getWords()) {
        	System.out.println(r);
            }

            System.out.println("Best 3 hypothesis:");            
            for (String s : result.getNbest(3))
                System.out.println(s);

            System.out.println("Lattice contains " + result.getLattice().getNodes().size() + " nodes");
        }

        recognizer.stopRecognition();

        
        System.out.println("The results with the adapted model");
        
        // the buffer is not available yet
        stream = TranscriberDemo.class.getResourceAsStream(
                    "/edu/cmu/sphinx/demo/MllrDecodingConfig/JamesCameron_2010_310.25_329.97.wav");
        
     
        recognizer.startRecognition(stream, true);
        System.out.println("REsult " + result);
        while ((result = recognizer.getResult()) != null) {
            
            System.out.format("Hypothesis: %s\n",
                              result.getHypothesis());
                              
            System.out.println("List of recognized words and their times:");
            for (WordResult r : result.getWords()) {
        	System.out.println(r);
            }

            System.out.println("Best 3 hypothesis:");            
            for (String s : result.getNbest(3))
                System.out.println(s);

            System.out.println("Lattice contains " + result.getLattice().getNodes().size() + " nodes");
        }

        recognizer.stopRecognition();
    	

        stream = TranscriberDemo.class.getResourceAsStream(
                "/edu/cmu/sphinx/demo/countsCollector/BillGates_2010_554.62_564.32.wav");
        
        // Adapt offline with the new acoustic model
        recognizer.startRecognition(stream, false);

        while ((result = recognizer.getResult()) != null) {
        
            System.out.format("Hypothesis: %s\n",
                              result.getHypothesis());
                              
            System.out.println("List of recognized words and their times:");
            for (WordResult r : result.getWords()) {
        	System.out.println(r);
            }

            System.out.println("Best 3 hypothesis:");            
            for (String s : result.getNbest(3))
                System.out.println(s);

            System.out.println("Lattice contains " + result.getLattice().getNodes().size() + " nodes");
        }

        recognizer.stopRecognition();
        
    }
}
