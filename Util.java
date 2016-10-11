/*
 *	File:   Util.java	
 *  Author: Jackson Davenport
 *
 *  Utility class to provide common space for simple tasks such as opening,
 *	reading, writing, (de)serializing data to files.  
 *
 *
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.Enumeration;

public class Util{

	public static final String TRAINING_FILES = "Training_Files";
	public static final String ROOT_PATH = System.getProperty("user.dir")+System.getProperty("file.separator");

	public static long prevTime;
	
	public Util(){
	
	
	}
	
	/*  printBigramDistribution
	 *	    Take the hashtable for the complete unigram distribution and print
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
		
	/*	getScanner
	 *		Find the file and create a scanner object, handle the error
	 */
	public static Scanner getScanner(String fileName){
		Scanner newScanner = null;
		String directoryPath = "";
		try{
			directoryPath = ROOT_PATH + TRAINING_FILES;
			File directory = new File(directoryPath);
			File file = new File(directory, fileName);
			newScanner = new Scanner(new FileReader(file));
		}
		catch (FileNotFoundException e){
			System.err.println("Scanner did not find file: " + fileName);
			System.err.println(e);
			System.exit(0);
		}
		return newScanner;
	}

	/*	openWriteFile
	 *		Open up a file to overwrite on, create it if it doesn't exist
	 */
	public static BufferedWriter openWriteFile(String fileName){
		try{
			String directoryPath = ROOT_PATH + TRAINING_FILES;
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

	/*	openReadFile
	 *		Open up a file to overwrite on, create it if it doesn't exist
	 */
	public static BufferedReader openReadFile(String fileName){
		String directoryPath = ROOT_PATH + TRAINING_FILES;
		try{
			File directory = new File(directoryPath);
			File file = new File(directory, fileName);
			if(!file.exists()){
				throw new FileNotFoundException();
			}
			return (new BufferedReader(new FileReader(file.getAbsoluteFile()))); 
		}
		catch(IOException e){
			System.err.println("Error:"+ directoryPath + "\\" + fileName + " - not found");
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}		
	
	/*	getNextLine
	 *		Get the next line in the BufferedReader, return null when at end
	 *		of file
	 */
	public static String getNextLine(BufferedReader br){
		try{
			String s = br.readLine();
			return s;
		}
		catch(IOException e){
			return null;
		}
	}
	
	/*	closeFile
	 *		Close the file and print the stack trace as necessary
	 */
	public static void closeFile(BufferedReader br){
		try{
			br.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
			
	}

	/*	closeFile
	 *		Close the file and print the stack trace as necessary
	 */
	public static void closeFile(BufferedWriter br){
		try{
			br.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
			
	}
	
	/*  serializeDistribution
	 *		Take the distribution object and serialize it into memory to the
	 *		given filename within the Training_Files directory
	 */
	public static void serializeDistribution(Distributions d, String fileName){
		// Output the distribution to serializable memory
		try{
			// Get and if necessary create the file
			String directoryPath = ROOT_PATH + TRAINING_FILES;
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
	
	/*  deserializeDistribution
	 *		Take the serialized distribution from the given filename located
	 *		within the Training_Files directory
	 */
	public static Distributions deserializeDistribution(String fileName){
		Distributions dist = null;
		try {
			String directoryPath = ROOT_PATH + TRAINING_FILES;
			File directory = new File(directoryPath);
			File file = new File(directory, fileName);
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			
			dist = (Distributions) in.readObject();
			in.close();
			fileIn.close();
		}
		catch(IOException i) {
			System.out.println("IO Exception on file: " + fileName);
			i.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException c) {
			System.out.println("Distribution class not found");
			c.printStackTrace();
			return null;
		}
		return dist;
	}
	
	/*	initTime
	 *		Initialize the start time
	 */
	public static void initTime(){
		prevTime = System.nanoTime();
	}
	
	/*	logTime
	 *		Print the phrase of the log time
	 */
	public static void logTime(String endPhrase){
		System.out.println("Took " + ((System.nanoTime() - prevTime)/1000000) + " " + endPhrase);
		prevTime = System.nanoTime();
	}
	
}