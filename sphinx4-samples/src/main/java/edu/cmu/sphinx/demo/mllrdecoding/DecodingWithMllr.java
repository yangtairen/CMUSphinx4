package edu.cmu.sphinx.demo.mllrdecoding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.MllrDecoding;
import edu.cmu.sphinx.demo.transcriber.TranscriberDemo;
import edu.cmu.sphinx.linguist.acoustic.tiedstate.Sphinx3Loader;

public class DecodingWithMllr {
	public static void main(String[] args) throws IOException, URISyntaxException {
		
		Configuration configuration = new Configuration();
		
		configuration.setAcousticModelPath("/home/gia/Work/CMUSphinx/Task6/en-us/");
		configuration.setDictionaryPath("/home/gia/Work/CMUSphinx/Task6/tedlium.dic");
		configuration.setLanguageModelPath("/home/gia/Work/CMUSphinx/Task6/cmusphinx-5.0-en-us.lm.dmp");
		
		StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = TranscriberDemo.class.getResourceAsStream(
                "/edu/cmu/sphinx/demo/mllrdecoding/JamesCameron_2010_310.25_329.97.wav");

		Sphinx3Loader loader = (Sphinx3Loader) recognizer.getLoader();
		System.out.println(loader);
		
		String filePath = "/home/gia/Work/CMUSphinx/Task1/mllr_setup/data/JamesCameron_2010/mllr_matrix";
		MllrDecoding test = new MllrDecoding(loader, filePath);
		test.decodeWithMllr();
		
		loader = test.getNewLoader();
		recognizer.startRecognition(stream, false);
		
		SpeechResult result;

		while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
        }
		recognizer.stopRecognition();
	}
}
 