package edu.cmu.sphinx.demo.mllrdecoding;

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
public class DecodingWithMllr {
	public static void main(String[] args) throws Exception {
		
		Configuration configuration = new Configuration();
		
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/acoustic/en-us/");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp");
		configuration.setAdaptationFile("/home/gia/Work/Task1/mllr_matrix");
		
		
		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = TranscriberDemo.class.getResourceAsStream(
                "/edu/cmu/sphinx/demo/mllrdecoding/JamesCameron_2010_310.25_329.97.wav");
		
		recognizer.startRecognition(stream);
		
		SpeechResult result;

		while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
		recognizer.stopRecognition();
	}
}
 