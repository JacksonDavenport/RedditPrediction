import sys
import os
from Converter import Converter

# Validate Input
if(len(sys.argv) < 2):
    print ("Invalid input, please include subreddit name(s)")
    sys.exit()

# Use specified subreddits or the top from the list
if(len(sys.argv) == 2 and sys.argv[1] == "top"):
    subreddit_list = Converter.makeSubredditList()
else:
    subreddit_list = sys.argv[1:]
	
# Diagnostics
print ("Generate Distributions for:")
for subreddit_name in subreddit_list:
    print ("\t/r/", subreddit_name)
	
# Scroll through each subreddit included
for subreddit_name in subreddit_list:
    command = "Java CreateDistributions " + subreddit_name
    os.system(command)

# Use this java application to lower case, sort, and remove duplicates
os.system('Java RefitDictionary dictionary.txt sortedDictionary.txt')


