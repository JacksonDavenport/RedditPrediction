import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class SimWrapper{

	public static void main(String[] args){
		//System.out.println("\tStart: TitleModeling");
		Util.initTime();
		PriorityQueue<Result> results = new PriorityQueue<Result>(10, new LikelihoodComparator());
		
		// Check input
		if(args.length != 2){
			System.out.println("Invalid Input: Include input [subreddit] [\"Example Title\"]");
			System.out.println("args.length = " + args.length);
			System.exit(0);
		}
		
		//Run for a list of subreddits
		if(args[0].equals("-t") || args[0].equals("-top")){
			ArrayList<String> topSubreddits = new ArrayList<String>();
			BufferedReader topSubredditsData = Util.openReadFile("Subreddits.txt");
			String currentLine = Util.getNextLine(topSubredditsData);
			while(currentLine != null){
				topSubreddits.add(currentLine);				
				currentLine = Util.getNextLine(topSubredditsData);
			}

			for(int i = 0; i < topSubreddits.size(); i++){
				double likelihood = SimWrapper.runForSubreddit(topSubreddits.get(i), args[1]);
				Result r = new Result(topSubreddits.get(i), likelihood);
				results.add(r);
			}
			
			System.out.println("Printing top subreddits");
			Result r = results.poll();
			int retainTopN = 10;
			while(r != null && retainTopN > 0){
				System.out.println(r.getSubreddit() + "\t" + r.getLikelihood());
				r = results.poll();
				retainTopN--;
			}
			
		}
		//Run for a specified subreddit
		else{
			SimWrapper.runForSubreddit(args[0], args[1]);
		}


		
		
	}
	
	public static double runForSubreddit(String subreddit, String phrase){		
		// Load up file readers based off of the subreddit passed in
		String fileNameInputUni = "Unigram_" + subreddit + ".txt";
		String fileNameInputBi  = "Bigram_" + subreddit + ".txt";
		BufferedReader readerUni = Util.openReadFile(fileNameInputUni);
		BufferedReader readerBi  = Util.openReadFile(fileNameInputBi);
		//System.out.println("Files Found");

		//                                                //
		// Rebuild the distributions given the text files //
		//                                                //
		RecreateDistributions rd = new RecreateDistributions();
		Distributions dist = rd.rebuildDistribution(subreddit, readerUni, readerBi);
		double likelihood = TitleModeling.runSim(dist, phrase);
		
		Util.logTime("milliseconds to determine optimal weights");
		System.out.println("\n----------------------------------");
		System.out.println("Subreddit     : /r/" + subreddit);
		System.out.println("Log Likelihood: " + likelihood);		
		System.out.println("----------------------------------");
		
		return likelihood;
	}
}