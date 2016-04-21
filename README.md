# RedditPrediction


Use a script to collect the titles from select/specified subreddits and use 
this collection to learn and predict the likelihood of a given title.

This is used to predict which subreddit a chosen title should belong too.
It calculates the log likelihood through unigram and bigram distributions.

### How To Run
Python Collect_Titles.py [subreddit1] [subreddit2] [...]  
Java RefitDictionary [total file] [output file]  
Java CreateDistributions [training file]  

### Program Description

Collect_Titles (Python)
  * Collect a set of titles per subreddit and use this set as a basis for the distributions
  * Uses PRAW to scroll through reddit and collect the titles
  * There is a field hardcoded within the file to say how many to collect per subreddit (MAX=1000)
  * Calls CreateDistributions and RefitDictionary per subreddit

CreateDistributions (Java)
  * Create the bigram and unigram distributions for a given subreddit
  * Determine the filenames using the naming convention supplied
  * Output the distributions to a file so you can access it later
  * Bigram Form: word | previous word | count(word|previous word)
  * Unigram From: word | count(word) 
  
RefitDictionary (Java)
  * Take a list of every single word gathered across all subreddits and create a dictionary
  * This dictionary is sorted, all lowercase, and unique
  
Additional Files
  * Converter.py - Used to help convert titles to a nice form to handle
  * BigramElement.java - Used to help create the Bigram Distribution


