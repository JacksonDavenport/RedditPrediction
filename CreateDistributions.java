/*
 *	File:   CreateDistributions.java	
 *  Author: Jackson Davenport
 *
 *  Takes in as the input the desired subreddit, and determines grabs the
 *  input file based on the naming convention used. This will create the 
 *  bigram and unigram distributions based off the dictionary list which
 *  is a file of the titles per subreddit. Outputs two files of the words
 *  counts in all lowercase as well.
 *
 *	Unigram: word | count
 *	Bigram:  word | previousword | count
 *
 */
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Enumeration;

public class CreateDistributions{

	public static void main(String[] args){
		System.out.println("Start: CreateDistribution");
		// Check input
		if(args.length != 1){
			System.out.println("Invalid Input: Include input [subreddit]");
			System.out.println("args.length = " + args.length);
			System.exit(0);
		}
		
		// Load up file reader and determine files based on naming convention and subreddit
		String fileNameInput = "Dictionary_" + args[0] + ".txt";
		String fileNameOutputUni = "Unigram_" + args[0] + ".txt";
		String fileNameOutputBi  = "Bigram_" + args[0] + ".txt";
		String fileNameOutputDi  = "Distribution_" + args[0] + ".ser";
		Scanner scan = Util.getScanner(fileNameInput);
		BufferedWriter writerUni = Util.openFile(fileNameOutputUni);
		BufferedWriter writerBi  = Util.openFile(fileNameOutputBi);
		System.out.println("Files Found");
			
		// Create the overlying unigram and bigram distribution table
		Distributions dist = new Distributions(args[0]);
		Hashtable<String, BigramElement> bigramDistribution = new Hashtable<String, BigramElement>(); 
		Hashtable<String, Integer> unigramDistribution = new Hashtable<String, Integer>();
		
		// Go through the file and grab it line by line
		int totalCount = 0;
		String inputLine, wordBase, wordPrev;
		while(scan.hasNextLine()){
			inputLine = (String) scan.nextLine();
			inputLine = inputLine.toLowerCase();
			ArrayList<String> textList = new ArrayList<String>(Arrays.asList(inputLine.split(" ")));
			//System.out.println(textList);  
		
			// Handle very first word specially
			wordBase = textList.get(0);
			dist.unigramAddWord(wordBase);
			if(!unigramDistribution.containsKey(wordBase)){
				unigramDistribution.put(wordBase, 1);
			}
			else{
				unigramDistribution.put(wordBase, unigramDistribution.get(wordBase) + 1);
			}
		
			// Fill in the elements for this String line starting with the 2nd word
			for(int i = 1; i < textList.size(); i++){
				wordBase = textList.get(i);
				wordPrev = textList.get(i-1);
				
				// Distribution
				dist.unigramAddWord(wordBase);
				dist.bigramAddWord(wordPrev, wordBase);
								
				// Bigram handling
				if(!bigramDistribution.containsKey(wordBase)){
					bigramDistribution.put(wordBase, new BigramElement(wordBase));
				}
				bigramDistribution.get(wordBase).addWord(wordPrev);
				
				// Unigram handling
				if(!unigramDistribution.containsKey(wordBase)){
					unigramDistribution.put(wordBase, 1);
				}
				else{
					unigramDistribution.put(wordBase, unigramDistribution.get(wordBase) + 1);
				}
			}
			totalCount++;
		}
		
		// Output the unigram distribution to the file
		Util.printUnigramDistribution(unigramDistribution, writerUni);
		
		// Output the bigram distribution to the file
		Util.printBigramDistribution(bigramDistribution, writerBi);
		
		// Output the distribution to serializable memory
		Util.serializeDistribution(dist, fileNameOutputDi);
		
		// Close the files
		try{
			writerUni.close();
			writerBi.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		System.out.println("Done: CreateDistribution");
	}
		
	
}