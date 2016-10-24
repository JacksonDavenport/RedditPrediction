/*
 *	File:   Result.java	
 *  Author: Jackson Davenport
 *
 *  Small Object for a result of a simulation.  Used as the object surrounding
 *  the priority queue to determine the most likely subreddit.
 */
public class Result{
	String subreddit;
	Double likelihood;
	
	public Result(String s, Double l){
		subreddit = s;
		likelihood = l;
	}
		
	public String getSubreddit(){
		return subreddit;
	}
	public Double getLikelihood(){
		return likelihood;
	}

}