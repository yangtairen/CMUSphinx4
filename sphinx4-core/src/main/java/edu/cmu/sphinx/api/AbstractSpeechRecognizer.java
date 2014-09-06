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

import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;

/**
 * Base class for high-level speech recognizers.
 */
public class AbstractSpeechRecognizer {

	protected final Context context;
	protected final Recognizer recognizer;
	
	

	protected final SpeechSourceProvider speechSourceProvider;
	public SpeakerProfile profile;

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
		
	}
	
	public void initAdaptation(Context c) throws Exception {
		profile = new SpeakerProfile(c);
		profile.initialization();
	}



	
    protected void adaptOnline(boolean ok, int ID) throws Exception {
 
    	SpeechResult result;
    	
    	while ((result = this.getResult()) != null) {
    		profile.cc.collect(result.getResult());
    	}
    	
    
        if(!ok) {
        	this.profile.adaptCurrentModel(ID);
        	this.profile.setCollectStatsForAdaptation(false);
        	
        }
    	
    }
    

	public void adaptOffline(int ID) throws IOException, URISyntaxException {
		profile.adapt(ID);
	}
	

	
	/**
	 * Returns result of the recognition.
	 */
	public SpeechResult getResult2() {
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
	
	public SpeechResult getResult() {
		
		Result result = recognizer.recognize();
		return null == result ? null : new SpeechResult(result);
	}
	
}
