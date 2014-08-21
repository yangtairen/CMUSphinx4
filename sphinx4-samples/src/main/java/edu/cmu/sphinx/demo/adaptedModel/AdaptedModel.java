package edu.cmu.sphinx.demo.adaptedModel;

import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.demo.transcriber.TranscriberDemo;
/**
 * 
 * @author Georgiana Chelu
 *
 */

public class AdaptedModel {
	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration();

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/acoustic/en-us/");
		configuration
				.setDictionaryPath("resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d");
		configuration
				.setLanguageModelPath("resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp");

		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(
				configuration);
		
		
		InputStream stream = TranscriberDemo.class
				.getResourceAsStream("/edu/cmu/sphinx/demo/mllrTransform/JamesCameron_2010_310.25_329.97.wav");
	
		recognizer.startRecognition(stream, true);
		recognizer.stopRecognition();
		
	
		// the buffer is not available yet
		StreamSpeechRecognizer r = new StreamSpeechRecognizer(configuration);
		InputStream s = TranscriberDemo.class
				.getResourceAsStream("/edu/cmu/sphinx/demo/mllrTransform/JamesCameron_2010_310.25_329.97.wav");
		

		r.startRecognition(s, false);
		
		SpeechResult result;

		// get the results with the adapted model
		while ((result = r.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
		r.stopRecognition();

	}

}
