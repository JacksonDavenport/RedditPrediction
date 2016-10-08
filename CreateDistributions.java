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
		Scanner scan = getScanner(fileNameInput);
		BufferedWriter writerUni = openFile(fileNameOutputUni);
		BufferedWriter writerBi  = openFile(fileNameOutputBi);
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
		printUnigramDistribution(unigramDistribution, writerUni);
		
		// Output the bigram distribution to the file
		printBigramDistribution(bigramDistribution, writerBi);
		
		// Output the distribution to serializable memory
		serializeDistribution(dist, fileNameOutputDi);
		
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
	
	
	/*  serializeDistribution
	 *		Take the distribution object and serialize it into memory.
	 */
	public static void serializeDistribution(Distributions d, String fileName){
		// Output the distribution to serializable memory
		try{
			// Get and if necessary create the file
			String directoryPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"Training_Files";
			File directory = new File(directoryPath);
			File file = new File(directory, fileName);
			if(!file.exists()){
				file.createNewFile();
			}		
        	FileOutputStream fileOut = new FileOutputStream(file);
			
			// Serialize it and output it
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(d);
			out.close();
			fileOut.close();
      	}catch(IOException i){
			i.printStackTrace();
			System.exit(0);
      	}
	}	
	
	/*  printBigramDistribution
	 *	    Take the hashtable for the complete bigram distribution and print
	 *	    it out nicely to a file
	 */
	public static void printUnigramDistribution(Hashtable<String, Integer> uni, BufferedWriter outFile){	
		Enumeration words = uni.keys();
		while(words.hasMoreElements()){
			String key = (String) words.nextElement();
			int value = uni.get(key);
			String toWrite = (key + "\t" + value);

			// Write to the file
			try{
				outFile.write(toWrite);
				if(words.hasMoreElements()){
					outFile.newLine();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}		
	}
	
	/*  printBigramDistribution
	 *	    Take the hashtable for the complete bigram distribution and print
	 *	    it out nicely to a file
	 */
	public static void printBigramDistribution(Hashtable<String, BigramElement> bd, BufferedWriter outFile){	
		Enumeration firstWord = bd.keys();
		while(firstWord.hasMoreElements()){
			String firstKey = (String) firstWord.nextElement();
			Enumeration secondWord = bd.get(firstKey).getKeys();
			while(secondWord.hasMoreElements()){
				String secondKey = (String) secondWord.nextElement();
				int value = bd.get(firstKey).getCount(secondKey);
				String toWrite = (firstKey + "\t" + secondKey + "\t" + value);

				// Write to the file
				try{
					outFile.write(toWrite);
					if(firstWord.hasMoreElements() || secondWord.hasMoreElements()){
						outFile.newLine();
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}		
	}
	
	/*  printBigramDistribution
	 *	    Take the hashtable for the complete bigram distribution and print
	 *	    it out nicely to console
	 */
	public static void printBigramDistribution(Hashtable<String, BigramElement> bd){
		Enumeration firstWord = bd.keys();
		while(firstWord.hasMoreElements()){
			String firstKey = (String) firstWord.nextElement();	//
			Enumeration secondWord = bd.get(firstKey).getKeys();
			while(secondWord.hasMoreElements()){
				String secondKey = (String) secondWord.nextElement();
				int value = bd.get(firstKey).getCount(secondKey);
				System.out.println(firstKey + "\t" + secondKey + "\t" + value);
			}
		}		
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

	/*
	 *	openFile(String filename)
	 *		Open up a file to overwrite on, create it if it doesn't exist
	 */
	public static BufferedWriter openFile(String fileName){
		try{
			String directoryPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"Training_Files";
			File directory = new File(directoryPath);
			File file = new File(directory, fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			return (new BufferedWriter(new FileWriter(file.getAbsoluteFile()))); 
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}	
	
}