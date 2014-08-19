package edu.cmu.sphinx.demo.mllrdecoding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.demo.transcriber.TranscriberDemo;
/**
 * Decoding with a specific mllr file
 * @author gia
 *
 */
public class DecodingWithMllr {
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		Configuration configuration = new Configuration();
		
		configuration.setAcousticModelPath("/home/gia/Work/Task1/en-us/");
		configuration.setDictionaryPath("/home/gia/Work/Task1/tedlium.dic");
		configuration.setLanguageModelPath("/home/gia/Work/Task1/cmusphinx-5.0-en-us.lm.dmp");
		configuration.setAdaptationFile("/home/gia/Work/Task1/mllr_matrix");
		
		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = TranscriberDemo.class.getResourceAsStream(
                "/edu/cmu/sphinx/demo/mllrdecoding/JamesCameron_2010_310.25_329.97.wav");

		recognizer.startRecognition(stream, false);
		
		SpeechResult result;

		while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
		recognizer.stopRecognition();
	}
}
 