import java.util.Comparator;

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