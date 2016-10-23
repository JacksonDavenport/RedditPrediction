/*
 *	File:   TitleModeling.java	
 *  Author: Jackson Davenport
 *
 *  Takes as input a subreddit and a phrase to test. This will load up and
 *  recreate the distributions saved and then check the probability of the
 *  given title according to the distribution belonging to this subreddit.
 */
import java.io.BufferedReader;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
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
		Util.initTime();
		// Check input
		if(args.length != 2){
			System.out.println("Invalid Input: Include input [subreddit] [\"Example Title\"]");
			System.out.println("args.length = " + args.length);
			System.exit(0);
		}
		
		String subreddit = args[0];
		String phrase = args[1];
		
		// Load up file readers based off of the subreddit passed in
		String fileNameInputUni = "Unigram_" + subreddit + ".txt";
		String fileNameInputBi  = "Bigram_" + subreddit + ".txt";
		BufferedReader readerUni = Util.openReadFile(fileNameInputUni);
		BufferedReader readerBi  = Util.openReadFile(fileNameInputBi);
		//System.out.println("Files Found");

		//                                                //
		// Rebuild the distributions given the text files //
		//                                                //
		RecreateDistributions rd = new RecreateDistributions();
		Distributions dist = rd.rebuildDistribution(subreddit, readerUni, readerBi);
		// Unigram Distribution
		Hashtable<String, Integer> unigramDistribution = dist.getUnigramDistribution();
		// Bigram Distribution
		Hashtable<String, BigramElement> bigramDistribution = dist.getBigramDistribution();
				
		Util.logTime("milliseconds to recreate distributions");
		
		//                              //
		// Mixture Model of probability //
		//                              //
		String baseWord, prevWord, currentLine;
		double totalUniCount = (double) dist.getTotalUni();		
		String test = phrase.toLowerCase();
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
				PuList.add((double) 0.000001);
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
				PbList.add((double) 0.000001);
			}
		}		

		Util.logTime("milliseconds to determine probability per word");
				
		//                                       //
		// Final Summation/Calculation/Weighting //
		//                                       //
		double likelihood = getOptimalWeights(PuList, PbList);
		Util.logTime("milliseconds to determine optimal weights");
		System.out.println("\n----------------------------------");
		System.out.println("Subreddit     : /r/" + subreddit);
		System.out.println("Log Likelihood: " + likelihood);		
		System.out.println("----------------------------------");
	}
	

	public static double getOptimalWeights(ArrayList<Double> PuList, ArrayList<Double> PbList){
		double optimalWeight = 0;
		double likelihood = Double.NEGATIVE_INFINITY;
		for(double y = 0; y <= 1; y += 0.0001){
			double summation = 0;
			for(int i = 0; i < PbList.size(); i++){
				// Lm = Summation(i) ( log[yPu(wi) + (1-y)Pb(wi|wi-1)] )
				summation += Math.log( y * PuList.get(i) + (1 - y) * PbList.get(i) );
			}

			if(summation > likelihood){
				optimalWeight = y;
				likelihood = summation;
			}
		}
		return likelihood;
	}
	
	public static double runSim(Distributions dist, String phrase){

		// Unigram Distribution
		Hashtable<String, Integer> unigramDistribution = dist.getUnigramDistribution();
		// Bigram Distribution
		Hashtable<String, BigramElement> bigramDistribution = dist.getBigramDistribution();
				
		Util.logTime("milliseconds to recreate distributions");
		
		//                              //
		// Mixture Model of probability //
		//                              //
		String baseWord, prevWord, currentLine;
		double totalUniCount = (double) dist.getTotalUni();		
		String test = phrase.toLowerCase();
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
				PuList.add((double) 0.000001);
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
				PbList.add((double) 0.000001);
			}
		}		

		Util.logTime("milliseconds to determine probability per word");
				
		//                                       //
		// Final Summation/Calculation/Weighting //
		//                                       //
		return getOptimalWeights(PuList, PbList);
	}
	
}

/*
Data via deserializing the data, however, results showed that it was about 3x slower to do so

String fileNameInputSer = "Distribution_" + subreddit + ".ser";
Distributions distribution = Util.deserializeDistribution(fileNameInputSer);
Hashtable<String, BigramElement> bigramDistribution = distribution.getBigramDistribution();
Hashtable<String, Integer> unigramDistribution = distribution.getUnigramDistribution();
double totalUniCount = (double) distribution.getTotalUni();
*/

