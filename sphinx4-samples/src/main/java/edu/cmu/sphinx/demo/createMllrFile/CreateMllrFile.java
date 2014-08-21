package edu.cmu.sphinx.demo.createMllrFile;

import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.demo.transcriber.TranscriberDemo;

/**
 * 
 * @author Georgiana Chelu
 *
 */
public class CreateMllrFile {
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
	
	while (recognizer.getResult() != null);
	
	recognizer.stopRecognition();
	recognizer.getFile();
		
		
	}
	
}
