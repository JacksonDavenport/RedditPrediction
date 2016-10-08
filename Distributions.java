import java.io.Serializable;
import java.util.Hashtable;

public class Distributions implements Serializable{

	public Hashtable<String, BigramElement> bigramDistribution;
	public Hashtable<String, Integer> unigramDistribution;
	
	public String subreddit;
	public int totalUni;
	
	public Distributions(String s){
		bigramDistribution  = new Hashtable<String, BigramElement>();
		unigramDistribution = new Hashtable<String, Integer>(); 

		subreddit = s;
		totalUni = 0;
	}
	
	public void bigramAddWord(String wordPrev, String wordBase){
		// Bigram handling
		if(!bigramDistribution.containsKey(wordBase)){
			bigramDistribution.put(wordBase, new BigramElement(wordBase));
		}
		bigramDistribution.get(wordBase).addWord(wordPrev);	
	}
	
	public void bigramAddWord(String wordPrev, String wordBase, int count){
		// Bigram handling
		if(!bigramDistribution.containsKey(wordBase)){
			bigramDistribution.put(wordBase, new BigramElement(wordBase));
		}
		bigramDistribution.get(wordBase).addWord(wordPrev, count);
	}
	
	public void unigramAddWord(String wordBase){
		// Unigram handling
		if(!unigramDistribution.containsKey(wordBase)){
			unigramDistribution.put(wordBase, 1);
		}
		else{
			unigramDistribution.put(wordBase, unigramDistribution.get(wordBase) + 1);
		}
	}
	
	public void unigramAddWord(String wordBase, int count){
		// Unigram handling
		if(!unigramDistribution.containsKey(wordBase)){
			unigramDistribution.put(wordBase, count);
		}
		else{
			unigramDistribution.put(wordBase, unigramDistribution.get(wordBase) + count);
		}	
	}
	
	public void setTotalUnigramCount(int count){
		totalUni = count;
	}
	public void incrementTotalUnigramCount(){
		totalUni++;
	}
	
	/*
	 *	Getter methods
	 */
	
	public Hashtable<String, BigramElement> getBigramDistribution(){
		return bigramDistribution;
	}
	public Hashtable<String, Integer> getUnigramDistribution(){
		return unigramDistribution;
	}
	public String getSubreddit(){
		return subreddit;
	}
	public int getTotalUni(){
		return totalUni;
	}

}