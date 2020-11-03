import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

/*
 * This class contains helping functions in order to visualize or use csv Datasets.
 * The minimum required version of Java is Jdk8.
 * 
 * Copyright 2020: Vaios Ampelakiotis
 */
public class CSVtools {

	public final static String [] legend={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","D","E","F","G","H","I","J","L","M","N","Q","R","T","U","Y","0","1","2","3","4","5","6","7","8","9",",","(",")","-","+","=","<",">","uniq","exiq","con","imp","bi","neg"};
	
	private int charDimension;
	private int inputSize;
	private final int outputSize= legend.length;
	private int totalSize;
	
	final int gapMargin= 5;
	private int width;
	private int height;
	
	String fileformat= "jpg";
	
	//This is the class constructor that takes dimension argument to calculate various sizes.
	//The only valid dimensions are 28 and 35.
	public CSVtools(int dimension) {
		charDimension= dimension;
		inputSize= charDimension*charDimension;
		totalSize= inputSize+outputSize;
		
		width= (charDimension+gapMargin)*outputSize;
	}
	
	/*
	 *This method reads a csv file from path on first argument and returns a map of all characters with
	 *their corresponding samples. The second argument must be the dimension of samples in Dataset.
	 *The third argument defines the limit of samples per character and finally all allowed
	 *samples are painted in a .jpg image on same path. Also, a map of characters with their 
	 *samples as BufferedImages is returned.
	 */
	public Map<String,List<BufferedImage>> printCsv(String csvfilepath, int limit ) {
		File csvfile= new File(csvfilepath);
		
		//check limit size
		if (limit<=0) { 
			System.out.println("Limit size must be >0");
			return null;
		}
		//check dimension size
		if (charDimension!=28 && charDimension!=35) {
			System.out.println("Dimension size must be 28 or 35!");
			return null;
		}
		
		//create a map of all characters with their data
		Map<String,List<byte[]>> allSamples= new HashMap<String,List<byte[]>>();
		for (String s: legend)
			allSamples.put(s, new ArrayList<byte[]>());
		
		//create a map of all characters as BufferedImage
		Map<String,List<BufferedImage>> images= new HashMap<String,List<BufferedImage>>();
		for (String s: legend)
			images.put(s, new ArrayList<BufferedImage>());
		
		
		try {
			BufferedReader br= new BufferedReader(new FileReader(csvfile));
			System.out.println("Reading CSV file...");
			
			String line= null;
			String[] tokens= null;
			String[] rowImage= null;
			String[] rowLabel= null;
			
			int maxIndex= 0;
			
			while ( (line=br.readLine())!=null ) {
				tokens= line.split(",");
				//check every line if has correct length
				if (tokens.length!= totalSize) {
					System.out.println("CSV file not valid! \n Total length of raw should be "+totalSize+
							" (input size: "+inputSize+" , label size: "+outputSize+")");
					br.close();
					return null;
				}
				//take parts of row as image and label
				rowImage= Arrays.copyOfRange(tokens, 0, inputSize);
				rowLabel= Arrays.copyOfRange(tokens, tokens.length-outputSize, tokens.length);
				
				//find character by max index of its' label
				for (int i=0; i<rowLabel.length; i++)
					if (rowLabel[i].equals("1")) maxIndex=i;
				
				//extract character's pixel values from row
				byte[] data= new byte[inputSize];
				for (int i=0; i<rowImage.length; i++)
					data[i]= (byte) Double.parseDouble(rowImage[i]);
								
				//Add new sample to corresponding character on map (if limit is not reached)
				if ( allSamples.get(legend[maxIndex]).size()<limit )
					allSamples.get(legend[maxIndex]).add(data);
			}
			br.close();
					
		} catch (FileNotFoundException e1) {
			System.out.println("File not found! Check path argument...");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
		//find character with most samples
		int maxSample=0;
		int listSize;
		for (Entry<String,List<byte[]>> entry:allSamples.entrySet()) {
			listSize= entry.getValue().size();
			if (listSize>maxSample) maxSample= listSize;
		}
		//calculate new height
		height= (charDimension+gapMargin)*maxSample;
		
		System.out.println("Printing character samples... \n");
		BufferedImage concatImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = concatImage.createGraphics();
        int stepH= 0;
		
		//Fill map of all characters with their corresponding BufferedImage  
		for (String ch : legend) {
			int stepV= 0;
			for (byte[] chardata : allSamples.get(ch)) {
							
				//create BufferedImage by data values and add to image map
				BufferedImage sample= new BufferedImage(charDimension, charDimension, BufferedImage.TYPE_INT_RGB);
				int datacounter=0;
				for (int i=0; i<charDimension; i++)
					for (int j=0; j<charDimension; j++) {
						if (chardata[datacounter++]!=0)
							sample.setRGB(j, i, Color.white.getRGB());
					}
				images.get(ch).add(sample);
				
				//draw sample in output image
				g2d.drawImage(sample, stepH, stepV, null);
	            stepV += charDimension+gapMargin;		
			}
			stepH += charDimension+gapMargin;
		}
		
		//define final name and save image to same path of .csv file
        String csvName= csvfile.getName().substring(0, csvfile.getName().indexOf("."));
        String imagepathname= csvfile.getParent()+"\\"+csvName+"-printed."+fileformat;
		try {
			ImageIO.write(concatImage, fileformat, new File(imagepathname));
			System.out.println("File saved to "+imagepathname);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//return a map of characters with lists of their samples as BufferedImage
		return images;
				
	}

	/*
	 * This is a the general method that paints and returns all csv file samples.
	 * For more info see 'printCsv(String,int)" method.
	 */
	public Map<String,List<BufferedImage>> printCsv(String csvfilepath) {
		return printCsv(csvfilepath, Integer.MAX_VALUE);
	}
	
	/*
	 * This method prints all character occurrences of a csv file to console.
	 */
	public void CSVstats(String csvfilepath) {
		File csvfile= new File(csvfilepath);
		
		//check dimension size
		if (charDimension!=28 && charDimension!=35) {
			System.out.println("Dimension size must be 28 or 35!");
			return;
		}		
		
		//create a map of all characters with their occurences
		Map<String,Integer> charOccurrences= new HashMap<String,Integer>();
		for (String s: legend)
			charOccurrences.put(s, 0);
		
		
		try {
			BufferedReader br= new BufferedReader(new FileReader(csvfile));
			System.out.println("Reading CSV file...");
			
			String line= null;
			String[] tokens= null;
			String[] rowLabel= null;
			
			int maxIndex= 0;
			
			while ( (line=br.readLine())!=null ) {
				tokens= line.split(",");
				//check every line if has correct length
				if (tokens.length!= totalSize) {
					System.out.println("CSV file not valid! \n Total length of raw should be "+totalSize+
							" (input size: "+inputSize+" , label size: "+outputSize+")");
					br.close();
					return;
				}
				//take label part
				rowLabel= Arrays.copyOfRange(tokens, tokens.length-outputSize, tokens.length);
				
				//find character by max index of its' label
				for (int i=0; i<rowLabel.length; i++)
					if (rowLabel[i].equals("1")) maxIndex=i;
				
				//increase character occurence
				charOccurrences.merge(legend[maxIndex],1, Integer::sum);
			}
			br.close();
			
			for (String ch : legend)
				System.out.println("'"+decoder(ch)+"' --> "+charOccurrences.get(ch));
					
		} catch (FileNotFoundException e1) {
			System.out.println("File not found! Check path argument...");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	//utility method that decodes special characters
	private String decoder(String s) {
		switch (s) {
			case "uniq":
				return "Universal quantifier";
			case "exiq":
				return "Existential quantifier";
			case "con":
				return "Conjunction";
			case "imp":
				return "Implication";
			case "bi":
				return "Equivalence";
			case "neg":
				return "Negation";
			default:
				return s;
		}
	}
	
	
}