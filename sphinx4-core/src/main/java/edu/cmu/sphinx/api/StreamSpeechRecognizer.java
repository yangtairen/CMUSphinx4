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
import java.io.InputStream;


/**
 * Speech recognizer that works with audio resources.
 *
 * @see LiveSpeechRecognizer live speech recognizer
 */
public class StreamSpeechRecognizer extends AbstractSpeechRecognizer {

    /**
     * Constructs new stream recognizer.
     *
     * @param configuration configuration
     */
    public StreamSpeechRecognizer(Configuration configuration)
        throws IOException
    {
        super(configuration);
    }
    
    private void adaptOnline(String path) throws Exception {
    	//TODO: access frontend for buffering the sent results
    	while (this.getResult() != null);
    	this.adaptCurrentModel(path);
    	this.profile.setCollectStatsForAdaptation(false);
    }

    /**
     * Starts recognition process.
     *
     * Starts recognition process and optionally clears previous data.
     *
     * @param clear clear cached microphone data
     * @throws Exception 
     * @see         StreamSpeechRecognizer#stopRecognition()
     */
    public void startRecognition(InputStream stream, boolean useOnlineAdaptation) throws Exception {
        recognizer.allocate();
        context.setSpeechSource(stream);
        
        String path = "/home/gia/Work/sphinx4/sphinx4-samples/src/main/resources/edu/cmu/sphinx/demo/transcriber/mllr_matrix";
        
        this.initAdaptation();
        
        if(useOnlineAdaptation) {
        	this.adaptOnline(path);
        }
        else {
        	this.adaptOffline(path);
        }
    }

    
    public void startRecognition(InputStream stream) throws Exception {
        recognizer.allocate();
        context.setSpeechSource(stream);
    }
    
	/**
     * Stops recognition process.
     *
     * Recognition process is paused until the next call to startRecognition.
     *
     * @see StreamSpeechRecognizer#startRecognition(boolean)
     */
    public void stopRecognition() {
        recognizer.deallocate();
    }
}
