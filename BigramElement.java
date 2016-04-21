import java.util.Hashtable;
import java.util.Enumeration;

/*
 *  File:   BigramElement
 *  Author: Jackson Davenport
 *
 *	This is a helper class of a single bigram element. An element is the base
 *  word and a hashmap of the previous words and their counts. This class
 *  handles the table operations while the structure to hold all these is held
 *  elsewhere.
 */
public class BigramElement{
	private String baseWord;
	private Hashtable<String, Integer> previousWord;
	private int totalCount;
		
	public BigramElement(String word){
		baseWord = word;
		previousWord = new Hashtable<String, Integer>();
		totalCount = 0;
	}
		
	/*  addWord(String word)
	 *	    Takes as input the previous word, so either increment the
	 *      count or add the new instance to the list
	 */
	public void addWord(String word){
		// If the word is already tracked increment the count
		if(previousWord.containsKey(word)){
			int value = previousWord.get(word) + 1;
			previousWord.put(word, value);
		}
		else{// Otherwise add it into the hash table with count = 1
			previousWord.put(word, 1);
		}
		totalCount++;
	}
	
	/*
	 *	Object Methods
	 */
	
	/*  int hashCode()
	 *	    Overwrite the hashcode method to return the hash of the baseword
	 */
	public int hashCode(){
		return baseWord.hashCode();
	}
	/*  String toString()
	 *	    Overwrite the toString method to print out the keys and counts
	 */
	public String toString(){
		String output = getWord() + " - [";
		Enumeration e = getKeys();
		while(e.hasMoreElements()){
			String key = (String) e.nextElement();
			output = output + key + ": " + getCount(key);
			if(e.hasMoreElements()){
				output = output + ", ";
			}
		}
		return output + "]";
	}
	/*  boolean equals(Object o)
	 *	    Check the baseword, total count, and individual counts
	 */
	public boolean equals(Object o){
		if(o == null || !(o instanceof BigramElement)){
			return false;
		}
		if(!((BigramElement) o).getWord().equals(this.getWord())){
			return false;
		}
		if(((BigramElement) o).getTotalCount() != this.getTotalCount()){
			return false;
		}
		
		return true;		
	}
	
	/*
	 *  Getter Methods
	 */
	  
	public int getCount(String word){
		return previousWord.get(word);
	}
	public int getTotalCount(){
		return totalCount;
	}
	public Enumeration getKeys(){
		return previousWord.keys();
	}
	public String getWord(){
		return baseWord;
	}
}