package edu.cmu.sphinx.decoder.adaptation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import edu.cmu.sphinx.linguist.acoustic.tiedstate.Sphinx3Loader;


public class MllrDecoding {

	private DensityFileData oldFileMeans;
	private float[][][][] A;
	private float[][][] B;
	private int nMllrClass;
	private Scanner input;
	private MllrTransformer mllrmat;
	private Sphinx3Loader loader;
//	private String location;
	private float varFlor;
//	private String model;
	private String mllrFilePath;



	public MllrDecoding(Sphinx3Loader loader, String mllrFilePath) {
		this.loader = loader;
		this.mllrFilePath = mllrFilePath;
//		this.location = "/home/gia/Work/CMUSphinx/me/en-us/";
//		this.model = "en-us";
		this.varFlor = (float) 1e-5;

	}
	
	public MllrDecoding() {
		
	}
	
	public void decodeWithMllr() throws IOException, URISyntaxException {
		readMllrMatrix(this.mllrFilePath);
		oldFileMeans = new DensityFileData();
		oldFileMeans.setNumGaussiansPerState(loader.getNumGaussiansPerState());
		oldFileMeans.setNumStates(loader.getNumStates());
		oldFileMeans.setNumStreams(loader.getNumStreams());
		oldFileMeans.setPool(loader.getMeansPool());
		oldFileMeans.setVarFloor(varFlor);
		oldFileMeans.setVectorLength(loader.getVectorLength());
		
		this.mllrmat = new MllrTransformer(oldFileMeans, A, B, "nothing");
		mllrmat.transform();
		loader.changeMeanFile(mllrmat.getMeans());
		
	}
	
	public Sphinx3Loader getNewLoader(){
		return loader;
	}
	/**
	 * Read the adaptation file 
	 * @param filePath
	 * @throws FileNotFoundException
	 */ 
	private void readMllrMatrix(String filePath) throws FileNotFoundException {

		input = new Scanner(new File(filePath));
		int[] vectorLength = new int[5];
		int numStreams;

		nMllrClass = input.nextInt();
		numStreams = input.nextInt();

		for(int m = 0; m < nMllrClass; m++) {
			for(int i = 0; i < numStreams; i++) {
				vectorLength[i] = input.nextInt();

				int length = vectorLength[i];

				A = new float[nMllrClass][numStreams][length][length];
				B = new float[nMllrClass][numStreams][length];


				for(int j = 0; j < length; j++) {
					for(int k = 0; k < length; ++k) {
						A[m][i][j][k] = input.nextFloat();
					}
				}

				for(int j = 0; j < length; j++) {
					B[m][i][j] = input.nextFloat();
				}
			}
		}
	}

}
