import sys
import os

# Validate Input
if(len(sys.argv) < 2):
    print ("Invalid input, please include subreddit name(s)")
    sys.exit()

# Diagnostics
print ("Generate Distributions for:")
for subreddit_name in (sys.argv[1:]):
    print ("\t/r/", subreddit_name)
	
# Scroll through each subreddit included
for subreddit_name in (sys.argv[1:]):
    command = "Java CreateDistributions " + subreddit_name
    os.system(command)

# Use this java application to lower case, sort, and remove duplicates
os.system('Java RefitDictionary dictionary.txt sortedDictionary.txt')


