/*
 *	File:   TitleModeling.java	
 *  Author: Jackson Davenport
 *
 *  Takes as input a subreddit and a phrase to test. This will load up and
 *  recreate the distributions saved and then check the probability of the
 *  given title according to the distribution belonging to this subreddit.
 */
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.Enumeration;

public class TitleModeling {

	public static void main(String[] args){
		//System.out.println("\tStart: TitleModeling");
		long startTime = System.nanoTime();
		// Check input
		if(args.length != 2){
			System.out.println("Invalid Input: Include input [subreddit] [\"Example Title\"]");
			System.out.println("args.length = " + args.length);
			System.exit(0);
		}
		
		// Load up file readers based off of the subreddit passed in
		String fileNameInputUni = "Unigram_" + args[0] + ".txt";
		String fileNameInputBi  = "Bigram_" + args[0] + ".txt";
		Scanner scanUni = getScanner(fileNameInputUni);
		Scanner scanBi  = getScanner(fileNameInputBi);
		//System.out.println("Files Found");

		//                                                //
		// Rebuild the distributions given the text files //
		//                                                //
		String[] currentLine;
		String baseWord, prevWord;
		int count;
		
		// Unigram Distribution
		Hashtable<String, Integer> unigramDistribution = new Hashtable<String, Integer>();
		double totalUniCount = 0;
		while(scanUni.hasNextLine()){
			// Split it over the tab
			currentLine = scanUni.nextLine().split("\t");
			baseWord = currentLine[0];
			count = Integer.parseInt(currentLine[1]);
			unigramDistribution.put(baseWord, count);
			totalUniCount += count;			
		}
		
		// Bigram Distribution
		Hashtable<String, BigramElement> bigramDistribution = new Hashtable<String, BigramElement>();
		while(scanBi.hasNextLine()){
			// Split it over the tab
			currentLine = scanBi.nextLine().split("\t");
			baseWord = currentLine[0];
			prevWord = currentLine[1];
			count = Integer.parseInt(currentLine[2]);

			// Add the word | prevWord | count to the set
			if(!bigramDistribution.containsKey(baseWord)){
				bigramDistribution.put(baseWord, new BigramElement(baseWord));
			}
			bigramDistribution.get(baseWord).addWord(prevWord, count);
		}
		long endTime = System.nanoTime();
		System.out.println("Took " + (endTime-startTime)/1000000 + " milliseconds to recreate distributions");
		startTime = endTime;
		
		//                              //
		// Mixture Model of probability //
		//                              //
		 
		String test = args[1].toLowerCase();
		//System.out.println("\t" + test);
		String[] parsedTest = test.split(" ");
		
		//Keep track of each iteration of probability
		ArrayList<Double> PuList = new ArrayList<Double>();
		ArrayList<Double> PbList = new ArrayList<Double>();
				
		//Find Pu(Word) = Pm(The) * Pm(stock) * ...
		for(int i = 0; i < parsedTest.length; i++){
			//Get the count of the word
			if(unigramDistribution.containsKey(parsedTest[i])){
				double PuCount = (double) unigramDistribution.get(parsedTest[i]);
				PuList.add(PuCount / totalUniCount);
			}
			else{
				PuList.add((double) 0.0000001);
			}
		}
				
		//Find Pb(Word | previous word)
		for(int i = 1; i < parsedTest.length; i++){
			//Get the count of the word
			baseWord = parsedTest[i];
			prevWord = parsedTest[i-1];
			if(bigramDistribution.containsKey(baseWord) && bigramDistribution.get(baseWord).containsKey(prevWord)){
				double PbCount = (double) bigramDistribution.get(baseWord).getCount(prevWord);
				double PbSize  = (double) bigramDistribution.get(baseWord).getTotalCount();
				PbList.add(PbCount / PbSize);
			}
			else{
				PbList.add((double) 0.0000001);
			}
		}		
		
		endTime = System.nanoTime();
		System.out.println("Took " + (endTime-startTime)/1000000 + " milliseconds to determine probability per word");
		startTime = endTime;
		
		// Print out probability lists
		/*
		System.out.println("Unigram Distribution");
		for(int i = 0; i < PuList.size(); i++){
			System.out.println("\t"+parsedTest[i]+"\t"+PuList.get(i));
		}
		System.out.println("Bigram Distribution");
		for(int i = 0; i < PbList.size(); i++){
			System.out.println("\t"+parsedTest[i+1]+"|"+parsedTest[i]+"\t"+PbList.get(i));
		}
		System.out.println("Done Printing");
		*/
		
		//                                       //
		// Final Summation/Calculation/Weighting //
		//                                       //
		double optimalY = 0;
		double previousOptimal = Double.NEGATIVE_INFINITY;
		for(double y = 0; y <= 1; y += 0.0001){
			double summation = 0;
			for(int i = 0; i < PbList.size(); i++){
				// Lm = Summation(i) ( log[yPu(wi) + (1-y)Pb(wi|wi-1)] )
				summation += Math.log( y * PuList.get(i) + (1 - y) * PbList.get(i) );
			}

			if(summation > previousOptimal){
				optimalY = y;
				previousOptimal = summation;
			}
		}
		endTime = System.nanoTime();
		System.out.println("Took " + (endTime-startTime)/1000000 + " milliseconds to determine optimal weights");
		startTime = endTime;
		System.out.println("\n----------------------------------");
		System.out.println("Subreddit     : /r/" + args[0]);
		System.out.println("Optimal unigram weight:" + optimalY);
		System.out.println("Log Likelihood: " + previousOptimal);		
		System.out.println("----------------------------------");
	}
	
	/*	getScanner(String filename)
	 *		Find the file and create a scanner object, handle the error
	 */
	public static Scanner getScanner(String fileName){
		Scanner newScanner = null;
		try{
			String directoryPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"Training_Files";
			File directory = new File(directoryPath);
			File file = new File(directory, fileName);
			newScanner = new Scanner(new FileReader(file));
		}
		catch (FileNotFoundException e){
			System.out.println("Did not find file: " + fileName);
			System.exit(0);
		}
		return newScanner;
	}
	
}

