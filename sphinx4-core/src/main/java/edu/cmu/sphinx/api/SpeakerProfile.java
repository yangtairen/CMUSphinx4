package edu.cmu.sphinx.api;

import java.io.IOException;
import java.net.URISyntaxException;

import edu.cmu.sphinx.decoder.adaptation.CountsCollector;
import edu.cmu.sphinx.decoder.adaptation.DensityFileData;
import edu.cmu.sphinx.decoder.adaptation.MllrDecoding;
import edu.cmu.sphinx.decoder.adaptation.MllrEstimation;
import edu.cmu.sphinx.decoder.adaptation.MllrTransformer;
import edu.cmu.sphinx.linguist.acoustic.tiedstate.Loader;
import edu.cmu.sphinx.linguist.acoustic.tiedstate.Sphinx3Loader;

public class SpeakerProfile {

	MllrDecoding test;
	
	private Context context;
	protected boolean collectStatsForAdaptation;
	private MllrEstimation estimation;
	private CountsCollector cc;
	
	Sphinx3Loader loader;
	
	SpeakerProfile() {
		
	}
	
	public SpeakerProfile(Context context) {
		this.context = context;
	}
	
	public void setCollectStatsForAdaptation(boolean status) {
		collectStatsForAdaptation = status;
	}
	
	public boolean getCollectStatsForAdaptation() {
		return collectStatsForAdaptation;
	}
	
	public CountsCollector getCountsCollector() {
		return cc;
	}
	
	public void initialization() {
		this.collectStatsForAdaptation = true;
		loader = (Sphinx3Loader) context.getLoader();
		this.cc = new CountsCollector(loader.getVectorLength(),
				loader.getNumStates(), loader.getNumStreams(),
				loader.getNumGaussiansPerState());
	}
	
//	public void load() throws FileNotFoundException {
//		test.readMllrMatrix(adaptationPath);
//	}
	
	public MllrTransformer getTransformation() throws Exception {
		DensityFileData means = new DensityFileData("", -Float.MAX_VALUE,
				loader, false);
		MllrTransformer transformer;

		if (!this.estimation.isComplete()) {
			this.estimation.estimateMatrices();
		}

		means.getMeansFromLoader();
		transformer = new MllrTransformer(means, estimation.getA(),
				estimation.getB(), "");
		transformer.transform();

		return transformer;
	}
	
	public void store(String path) throws Exception {
		this.estimation.setOutputFilePath(path);
		this.estimation.createMllrFile();		
	}
	
	public void reestimate() throws Exception{
	
		this.estimation = new MllrEstimation("", 1, "", false, cc.getCounts(),
				"", false, this.getLoader());
//		MllrTransformer transformer = this.getTransformation();
		// change the means when the buffer will be available 
//		loader.changeMeanFile(transformer.getMeans());
		
		this.estimation.estimateMatrices();
		
	}
	
	/**
	 * Returns the Loader object used for loading the acoustic model.
	 */
	public Loader getLoader() {
		return (Loader) context.getLoader();
	}

	public void adapt(String path) throws IOException, URISyntaxException {
		
		test = new MllrDecoding(loader, path);
		
		test.decodeWithMllr();
	}
}
