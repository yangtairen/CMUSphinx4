package edu.cmu.sphinx.api;

import java.io.IOException;
import java.net.URISyntaxException;

import edu.cmu.sphinx.decoder.adaptation.CountsCollector;
import edu.cmu.sphinx.decoder.adaptation.DensityFileData;
import edu.cmu.sphinx.decoder.adaptation.MllrDecoding;
import edu.cmu.sphinx.decoder.adaptation.MllrEstimation;
import edu.cmu.sphinx.decoder.adaptation.MllrTransformer;
import edu.cmu.sphinx.linguist.acoustic.tiedstate.Sphinx3Loader;

public class SpeakerProfile {

	MllrDecoding test;
	
	private Context context;
	protected boolean collectStatsForAdaptation;
	private MllrEstimation estimation;
	public CountsCollector cc;
	public DensityFileData means;
	
	Sphinx3Loader loader;

	
	SpeakerProfile() {
		
	}
	
	protected void adaptCurrentModel(int ID) throws Exception {
		this.reestimate();
		this.store(ID);
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
	
	public void initialization() throws IOException, URISyntaxException {
		this.collectStatsForAdaptation = true;
		loader = (Sphinx3Loader) context.getLoader();
		// e doar un constructor!
		this.cc = new CountsCollector(loader.getVectorLength(),
				loader.getNumStates(), loader.getNumStreams(),
				loader.getNumGaussiansPerState());
	}
	
	
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
	
	public void store(int ID) throws Exception {

		String path = "/home/gia/Work/mllr_matrix" + ID;

		this.estimation.setOutputFilePath(path);
		this.estimation.createMllrFile();		
	}
	
	public void reestimate() throws Exception{
	

		this.estimation = new MllrEstimation("", 1, "", false, this.cc.getCounts(),
				"", false, this.loader);
		
		this.estimation.estimateMatrices();
		
	}
	
	public void adapt(int ID) throws IOException, URISyntaxException {
		
		String path = "/home/gia/Work/mllr_matrix" + ID;
		System.out.println("Good Path " + path);
		
		test = new MllrDecoding(loader, path);
		
		loader.changeMeanFile(test.getTransformer().getMeans());
	}
}
