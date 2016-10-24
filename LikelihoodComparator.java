/*
 *	File:   LikelihoodComparator.java	
 *  Author: Jackson Davenport
 *
 *  Comparator class used with the Result priority queue to determine the more
 *  likely subreddit.  For log likelihoods like these all values are negative,
 *  with the more likely ones are values closer to 0.
 */
import java.util.Comparator;

public class LikelihoodComparator implements Comparator<Result> {
	
	
	@Override
	public int compare(Result r1, Result r2) {
		if(r2.getLikelihood() > r1.getLikelihood()){
			return 1;
		}
		if(r2.getLikelihood() < r1.getLikelihood()){
			return -1;
		}
		return 0;
	}
}