/*
 *	File:   RecreateDistributions.java	
 *  Author: Jackson Davenport
 *
 *  Takes as input two files which are the raw distributions as text files.  
 *  This file will take those and rebuild the distributions passing back 
 *  the full Distributions object.
 */
import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.Enumeration;

public class RecreateDistributions{

	public RecreateDistributions(){}
	
	public Distributions rebuildDistribution(String subreddit, BufferedReader readerUni, BufferedReader readerBi){
		String[] parsedLine;
		String baseWord, prevWord, currentLine;
		int count;
		int totalUniCount = 0;
		
		// Unigram Distribution
		Hashtable<String, Integer> unigramDistribution = new Hashtable<String, Integer>();
		// Bigram Distribution
		Hashtable<String, BigramElement> bigramDistribution = new Hashtable<String, BigramElement>();
		
		currentLine = Util.getNextLine(readerUni);
		while(currentLine != null){
			// Split it over the tab
			parsedLine = currentLine.split("\t");
			baseWord = parsedLine[0];
			count = Integer.parseInt(parsedLine[1]);
			unigramDistribution.put(baseWord, count);
			totalUniCount += count;			
			currentLine = Util.getNextLine(readerUni);
		}
		
		currentLine = Util.getNextLine(readerBi);
		while(currentLine != null){
			// Split it over the tab
			parsedLine = currentLine.split("\t");
			baseWord = parsedLine[0];
			prevWord = parsedLine[1];
			count = Integer.parseInt(parsedLine[2]);

			// Add the word | prevWord | count to the set
			if(!bigramDistribution.containsKey(baseWord)){
				bigramDistribution.put(baseWord, new BigramElement(baseWord));
			}
			bigramDistribution.get(baseWord).addWord(prevWord, count);
			currentLine = Util.getNextLine(readerBi);
		}

		return new Distributions(subreddit, totalUniCount, unigramDistribution, bigramDistribution);
	}

}