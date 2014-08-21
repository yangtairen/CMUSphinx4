/*
 * Copyright 2013 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

package edu.cmu.sphinx.api;

import java.io.IOException;
import java.net.URISyntaxException;

import edu.cmu.sphinx.linguist.acoustic.tiedstate.Loader;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;

/**
 * Base class for high-level speech recognizers.
 */
public class AbstractSpeechRecognizer {

	protected final Context context;
	protected final Recognizer recognizer;

	protected final SpeechSourceProvider speechSourceProvider;
	public final SpeakerProfile profile;

	/**
	 * Constructs recognizer object using provided configuration.
	 */
	public AbstractSpeechRecognizer(Configuration configuration)
			throws IOException {
		this(new Context(configuration));
	}

	protected AbstractSpeechRecognizer(Context context) throws IOException {
		this.context = context;
		recognizer = context.getInstance(Recognizer.class);
		speechSourceProvider = new SpeechSourceProvider();
		profile = new SpeakerProfile(this.context);
	}

	public void initAdaptation() throws Exception {
		profile.initialization();
		this.setAdaptationPath("/home/gia/Work/Task1/mllr_matrix");
	}

	protected void adaptCurrentModel() throws Exception {
		profile.reestimate();
		
	}

	public void adaptOffline() throws IOException, URISyntaxException {
		profile.adapt();
	}
	
	public void setAdaptationPath(String filePath) {
		profile.adaptationPath = filePath;
		
	}
	
	public void getFile() throws Exception {
		profile.getAdaptationFile();
	}
	/**
	 * Returns result of the recognition.
	 */
	public SpeechResult getResult() {
		Result result = recognizer.recognize();

		if (profile.getCollectStatsForAdaptation() && result != null) {
			try {
				profile.getCountsCollector().collect(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null == result ? null : new SpeechResult(result);
	}
	
	/**
	 * Returns the Loader object used for loading the acoustic model.
	 */
	public Loader getLoader() {
		return (Loader) context.getLoader();
	}
	
}
