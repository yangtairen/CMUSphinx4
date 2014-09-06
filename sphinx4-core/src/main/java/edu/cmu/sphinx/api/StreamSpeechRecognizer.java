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
import java.net.URL;
import java.util.ArrayList;

import edu.cmu.sphinx.linguist.acoustic.tiedstate.Sphinx3Loader;
import edu.cmu.sphinx.speakerid.Segment;
import edu.cmu.sphinx.speakerid.SpeakerCluster;
import edu.cmu.sphinx.speakerid.SpeakerIdentification;
import edu.cmu.sphinx.util.TimeFrame;


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

    public void startRecognition(InputStream stream) {
        startRecognition(stream, TimeFrame.INFINITE);
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
//    public void startRecognition(InputStream stream, boolean useOnlineAdaptation, boolean ok) throws Exception {
//        recognizer.allocate();
//        
//        
//        context.setSpeechSource(stream);
//        
//        
//        this.initAdaptation();
//        
//        if(useOnlineAdaptation) {
//        	this.adaptOnline(ok);
//        }
//        else {
//        	this.adaptOffline();
//        }
//    }
    public void startRecognition(URL url, boolean useOnlineAdaptation) throws Exception {

    	  collectStatsFromChuncks(url);
    	
//        recognizer.allocate();
//        context.setSpeechSource(stream);
//        
//        this.initAdaptation();
//        
//        if(useOnlineAdaptation) {
//        	this.adaptOnline(false);
//        }
//        else {
//        	this.adaptOffline();
//        }
    }
    
    
    public void startRecognition(InputStream stream, TimeFrame timeFrame) {
        recognizer.allocate();
        context.setSpeechSource(stream, timeFrame);
    }

    
//    public void startRecognition(InputStream stream) throws Exception {
//        recognizer.allocate();
//        context.setSpeechSource(stream);
//    }
    
    
    // collect stats for each chunck 
    public void collectStatsFromChuncks(URL url) throws Exception {
    	recognizer.allocate();
    
		SpeakerIdentification sd = new SpeakerIdentification();
		ArrayList<SpeakerCluster> speakers = sd.cluster(url.openStream());
		
		
		int size = speakers.size();
		p = new SpeakerProfile[size];

		ArrayList<Segment> segment = new ArrayList<Segment>();

    	TimeFrame t;
    	int ok;
    	
    	int idx = 0;
    	for (SpeakerCluster spk : speakers) {
    		segment = spk.getArrayOfSegments();

    		ok = segment.size();
    		
    		for(Segment s : segment) {
    			
    		   	long startTime = s.getStartTime();
            	long endTime   = s.getStartTime() + s.getLength();
            	t = new TimeFrame(startTime, endTime);
            	
                 
            	 context.setSpeechSource(url.openStream(), t);
                 p[idx] = new SpeakerProfile(context);
                 
                 this.initAdaptation(idx);
                 
            	// collect stats
            	if(ok == 1) {
            		this.adaptOnline(false, idx);
            	}
            	else
            		this.adaptOnline(true, idx);
                ok--;
             
    		}
    		idx++;
    	}
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


	public Sphinx3Loader getLoader() {
		// TODO Auto-generated method stub
		return (Sphinx3Loader)context.getLoader();
	}
}
