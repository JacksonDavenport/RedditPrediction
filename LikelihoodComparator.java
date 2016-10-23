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