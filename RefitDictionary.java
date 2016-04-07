/*
 *	File:   RefitDictionary.java	
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
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Enumeration;

public class RefitDictionary{

	public static void main(String[] args){
		// Check input
		if(args.length != 2){
			System.out.println("Invalid Input: Include two filenames");
			System.out.println("args.length = " + args.length);
			System.exit(0);
		}
		
		// Load up file reader
		String fileNameInput = args[0];
		String fileNameOutput = args[1];
		Scanner dicScan = getScanner(fileNameInput);
		System.out.println("Files Found");
		
		// Get number of words in list
		int totalWords = 0;
		while(dicScan.hasNextLine()){
			dicScan.nextLine();
			totalWords++;
		}		
		
		// Get rid of duplicates and single letter words
		int uniqueWords = 0;
		dicScan = getScanner(fileNameInput);
		Hashtable<String, Boolean> unsortedDictionary = new Hashtable<String, Boolean>();
		for(int i = 0; i < totalWords; i++){
			String input = (String) dicScan.nextLine();
			input = input.toLowerCase();
			if(!unsortedDictionary.containsKey(input) && input.length() > 1){
				uniqueWords++;
				unsortedDictionary.put(input, true);
			}
		}
		System.out.println("Unique/Total = "+uniqueWords+"/"+totalWords);
		
		// Sort the dictionary (for look not performance)
		Enumeration words = unsortedDictionary.keys();
		String[] sortedWords = new String[uniqueWords];
		int index = 0;
		while(words.hasMoreElements()){
			sortedWords[index] = (String) words.nextElement();
			index++;
		}
		Arrays.sort(sortedWords);
		
		// Rewrite it to the file
		BufferedWriter writer = openFile(fileNameOutput);
		for(int i = 0; i < sortedWords.length; i++){
			try{
				writer.write(sortedWords[i]);
				if(i != sortedWords.length-1){
					writer.newLine();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		// Close the file
		try{
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		System.out.println("Done");
	
	}

	//Find the file and create a scanner object
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
	
	//Fill up the dictionary
	static String[] fillDictionary(Scanner ai, int size){
		String[] dictionary = new String[size];
		for(int i = 0; i < size; i++){
			dictionary[i] = (String) ai.nextLine();
		}
		return dictionary;
	}
	
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