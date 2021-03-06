package edu.cmu.sphinx.decoder.adaptation;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class is used for reading and storing counts from file
 */
public class CountsReader {

	private String filePath;
	private Counts counts;

	public CountsReader(String filepath) {
		super();
		this.counts = new Counts();
		this.filePath = filepath;
	}

	public CountsReader() {
		this.counts = new Counts();
	}

	public Counts getCounts() {
		return this.counts;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Swaps bytes of an integer number
	 * 
	 * @param number
	 *            integer number to swap
	 * @return swapped integer value
	 */
	public int swapInt(int number) {
		int swapped = 0, byte1, byte2, byte3, byte4;

		byte1 = (number >> 0) & 0xff;
		byte2 = (number >> 8) & 0xff;
		byte3 = (number >> 16) & 0xff;
		byte4 = (number >> 24) & 0xff;
		swapped = byte1 << 24 | byte2 << 16 | byte3 << 8 | byte4 << 0;

		return swapped;
	}

	/**
	 * Reads integer from input stream and swaps its bytes
	 * 
	 * @param is
	 *            input stream to read from
	 * @return swapped integer value of the number read from input stream
	 */
	public int swappedReadInt(DataInputStream is) {
		int number, swapped = 0;

		try {
			number = is.readInt();
			swapped = swapInt(number);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return swapped;
	}

	/**
	 * Reads float from input stream and swaps its bytes
	 * 
	 * @param is
	 *            input stream to read from
	 * @return swapped float value of the number read from input stream
	 */
	public float swappedReadFloat(DataInputStream is) {
		int number, swappedInt;
		float swappedFloat = 0;

		try {
			number = Float.floatToIntBits(is.readFloat());
			swappedInt = swapInt(number);
			swappedFloat = Float.intBitsToFloat(swappedInt);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return swappedFloat;
	}

	/**
	 * Used for reading the mean and variance 3d-arrays. The method reads all
	 * data in one 1d-array and puts it in a 3d-array
	 * 
	 * @param n
	 *            the number of float numbers to be read
	 * @param d1
	 *            first dimension of the array
	 * @param d2
	 *            second dimension of the array
	 * @param d3
	 *            third dimension of the array
	 * @param is
	 *            input stream to read from
	 * @return the 3d-array read from input stream
	 */
	float[][][][] read4dArray(int n, int d1, int d2, int d3, DataInputStream is) {
		float[][][][] array;
		float[] buf = null;
		buf = new float[n];

		for (int i = 0; i < n; i++) {
			buf[i] = swappedReadFloat(is);
		}

		array = new float[d1][d2][d3][counts.getVeclen()[0]];

		for (int i = 0, bi = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				for (int k = 0; k < d3; k++) {
					for (int l = 0; l < counts.getVeclen()[j]; l++) {
						array[i][j][k][l] = buf[bi + l];
					}
					bi += counts.getVeclen()[j];
				}
			}
		}

		return array;
	}

	/**
	 * Reads the denominator 3d-array
	 * 
	 * @param is
	 *            input stream to read from
	 * @throws Exception
	 *             in case of array dimensions mismatch
	 */
	void readDnomArray(DataInputStream is) throws Exception {
		int d1, d2, d3, n, nc;
		float[] buf = null;

		d1 = swappedReadInt(is);
		d2 = swappedReadInt(is);
		d3 = swappedReadInt(is);
		n = swappedReadInt(is);
		buf = new float[n];

		for (int i = 0; i < n; i++) {
			buf[i] = swappedReadFloat(is);
		}

		if (n != d1 * d2 * d3) {
			throw new Exception("Dimensions mismatch!");
		}

		counts.setDnom(new float[d1][d2][d3]);
		nc = 0;

		for (int i = 0; i < d1; i++) {
			for (int j = 0; j < d2; j++) {
				for (int k = 0; k < d3; k++) {
					counts.getDnom()[i][j][k] = buf[nc++];
				}
			}
		}
	}

	/**
	 * Reads and stores in the class fields all data from the binary file
	 * provided in the filepath field.
	 */
	@SuppressWarnings("unused")
	public void read() throws Exception {

		int bom, hasMeans, hasVars, n;
		FileInputStream fp = null;
		DataInputStream is = null;
		byte[] ba = null;

		try {
			fp = new FileInputStream(filePath);
			is = new DataInputStream(fp);
			ba = new byte[40];
			is.read(ba, 0, 40);

			for (int i = 0; i < ba.length; i++) {
				counts.setHeader(counts.getHeader() + (char) ba[i]);
			}

			bom = is.readInt();
			hasMeans = swappedReadInt(is);
			hasVars = swappedReadInt(is);
			counts.setPass2var(swappedReadInt(is));
			counts.setnCb(swappedReadInt(is));
			counts.setnDensity(swappedReadInt(is));
			counts.setnFeat(swappedReadInt(is));
			
			counts.setVeclen(new int[counts.getnFeat()]);

			for (int i = 0; i < counts.getnFeat(); i++) {
				counts.getVeclen()[i] = swappedReadInt(is);
			}

			n = swappedReadInt(is);

			if (hasMeans == 1) {
				counts.setMean(this.read4dArray(n, counts.getnCb(), counts.getnFeat(),
						counts.getnDensity(), is));
			} else {
				throw new Exception("No means available!");
			}

			n = swappedReadInt(is);

			if (hasVars == 1) {
				counts.setVariance(this.read4dArray(n, counts.getnCb(), counts.getnFeat(),
						counts.getnDensity(), is));
			} else {
				throw new Exception("No variances available!");
			}

			this.readDnomArray(is);

			fp.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}