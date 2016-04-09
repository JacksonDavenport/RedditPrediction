/*
 *	File:   CreateDistributions.java	
 *  Author: Jackson Davenport
 *
 *  Takes in as the input two filenames. One is the filename of the list of
 *  all words which contains duplciates and is unsorted. This will take that
 *  text file and convert it to sorted, unique, and all lowercase and output 
 *  it to the filename specified as the 2nd input.
 */

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
		if(args.length != 2){
			System.out.println("Invalid Input: Include two filenames");
			System.out.println("args.length = " + args.length);
			System.exit(0);
		}
		
		// Load up file reader
		String fileNameInput = args[0];
		String fileNameOutput = args[1];
		Scanner scan = getScanner(fileNameInput);
		BufferedWriter writer = openFile(fileNameOutput);
		System.out.println("Files Found");
			
		// Create the overlying distribution table
		Hashtable<String, BigramElement> bigramDistribution = new Hashtable<String, BigramElement>(); 
		 
		// Go through the file and grab it line by line
		while(scan.hasNextLine()){
			String inputLine = (String) scan.nextLine();
			inputLine = inputLine.toLowerCase();
			ArrayList<String> textList = new ArrayList<String>(Arrays.asList(inputLine.split(" ")));
			//System.out.println(textList);  
		
			// Fill in the elements for this String line
			for(int i = 0; i < textList.size()-1; i++){
				String wordBase = textList.get(i);
				String wordFollow = textList.get(i+1);
				if(!bigramDistribution.containsKey(wordBase)){
					BigramElement returnedBD = bigramDistribution.put(wordBase, new BigramElement(wordBase));
				}
				bigramDistribution.get(wordBase).addWord(wordFollow);
			}
		}
		
		// Output the bigram distribution to the file
		printBigramDistribution(bigramDistribution, writer);
		
		// Close the file
		try{writer.close();}
		catch(IOException e){e.printStackTrace();}
		
		System.out.println("Done: CreateDistribution");
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
	public static Scanner getScanner(String filename){
		Scanner newScanner = null;
			try{
			newScanner = new Scanner(new FileReader(filename));
		}
		catch (FileNotFoundException e){
			System.out.println("Did not fine file: " + filename);
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
			File file = new File(fileName);
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