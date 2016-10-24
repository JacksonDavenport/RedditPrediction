# RedditPrediction


Use a script to collect the titles from select/specified subreddits and use 
this collection to learn and predict the likelihood of a given title.

This is used to predict which subreddit a chosen title should belong too.
It calculates the log likelihood through unigram and bigram distributions.

### How To Run
Python Collect_Titles.py [subreddit1] [subreddit2] [...]  
Python Generate_Distributions.py [subreddit1] [subreddit2] [...]  
Python Find_Best_Fit.py [subreddit1 [subreddit2] [...]  
Java RefitDictionary [total file] [output file]  
Java CreateDistributions [training file]  
Java TitleModeling [subreddit] ["phrase to check likelihood for"]
Java SimWrapper [subreddit] ["phrase to check likelihood for"]


### Program Description

SimWrapper (Java)
  * Run the TitleModeling simulation through a controller which handles loading the distributions
  * Can be run with a single subreddit or the top subbreddits by the -t or -top flag

Collect_Titles (Python)
  * Collect a set of titles per subreddit and use this set as a basis for the distributions
  * Uses PRAW to scroll through reddit and collect the titles
  * There is a field hardcoded within the file to say how many to collect per subreddit (MAX=1000)

Generate_Distributions (Python)
  * Run the Java program CreateDistributions for a list of subreddits
  * Calls RefitDictionary
  
Find_Best_Fit (Python)
  * Run the Java program TitleModeling over the list of included subreddits
  
CreateDistributions (Java)
  * Create the bigram and unigram distributions for a given subreddit
  * Determine the filenames using the naming convention supplied
  * Output the distributions to a file so you can access it later
  * Bigram Form: word | previous word | count(word|previous word)
  * Unigram From: word | count(word) 
  
RefitDictionary (Java)
  * Take a list of every single word gathered across all subreddits and create a dictionary
  * This dictionary is sorted, all lowercase, and unique

TitleModeling (Java)
  * Load up the distributions given the subreddit and the corresponding files
  * Determind the probability for each state (word)
  * Determing the optimal weighting between unigram and bigram probability
  * Report the log likelihood of the whoe title
  
Additional Files
  * Converter.py - Used to help convert titles to a nice form to handle and subreddits to list
  * BigramElement.java - Used to help create the Bigram Distribution
  * Distributions.java - Used to hold and manage the Distributions (Hashmaps)
  * RecreateDistributions.java - Used to compartmentalize reading the data file re-creating the distributions
  * Util.java - Utility class used to help manage file management, reader/writers, logging, etc.
  * Results.java - Simple Object to hold the likelihood and Subreddit, used with the priority queue
  * LikelihoodComparator.java - Simple Comparator to be used with Results for the priority queue

### Additional Details  

##### Accessing Reddit
The script acesses reddit through PRAW. Reddit requires bots to not make requests more than 30
requests per minute so it is built in to make a request for data every 2 seconds. Each request can
fetch 100 posts.  Reddit also only caches the top 1,000 posts per time range (new, hot, top).

##### Folder System
This set of programs will generate the data used for the prediction into text 
files, if run to its full extent it can generate for a list of N subreddits. This 
as of now generates 3 files per subreddit: Dictionary/Training File, Unigram Distribution,
and Bigram Distribution. As to not clog up the file Collect_Titles being the first one to run
attempts to also create the folder Training_Files to store all of these.

##### Top Subreddits
When running the Python scripts if you pass in "top" as the only parameter it will scroll through
the list of the top subreddits. This list was gathered from <http://redditlist.com/> and copy and
pasted in a file called "TopSubreddits.txt" within the Training_Files folder. A script in Converter
converts it from its nasty formatting into a list for the python scripts and Java apps to use.

This "top" parameter will allow you to collect and learn from as much data as you want.

##### Time
The program makes efficient use of hashtables to model and learn from the distributions and to 
perform the prediction.  The time cost of it has been drastically cut down since the prototype.
However the stall is largely due to:
1) Accessing Reddit itself for the data as their are caps on requests per minute
2) Creating the distributions from the initial data

Moving from Scanner to BufferedReaders as well as a wrapper for creating the Distribution has 
massively increased the speed of program.  As of now once the distributions are created the 
simulation can run through the top 250 subreddits and determine order the likelihood of each
in under 1.5 seconds.

##### Inspiration
This was inspired by a desire to practice Python, a personal desire to see if its possible to model
and predict titles off of Reddit, and make combine this with an algorithm learned through my studies
at UCSD. The algorithm was developed for a Artificial Intelligence class via probabilistic reasoning.  
