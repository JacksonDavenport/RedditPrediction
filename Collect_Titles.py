import praw
import webbrowser
import sys
import os
import math
from Converter import Converter

# Validate Input
if(len(sys.argv) < 2):
    print ("Invalid input, please include subreddit name(s)")
    sys.exit()
# Diagnostics
for subreddit_name in (sys.argv[1:]):
    print ("Search: /r/", subreddit_name)
	
# Open the reddit bot
user_agent = ("j1davenp Learning0.1")
r = praw.Reddit(user_agent = user_agent)

# Open the file to build the overall dictionary
totalFile = open(('Training_Files\\' + 'dictionary.txt'), 'w+')

# Begin collection, reddit max size allowed is 1000
print ("Begin Collection\n")
count = 1
size = 10

# Scroll through each subreddit included
for subreddit_name in (sys.argv[1:]):
    # Open the subreddit
    subreddit = r.get_subreddit(subreddit_name)
    print ("Begin collection for /r/", subreddit_name)

    # Open the file for the subreddit
    trainFileName = "Training_Files\\Dictionary_" + subreddit_name + ".txt"
    trainFile = open(trainFileName, 'w+')
	
    count = 1;	
    # Scroll through top 'size' posts of all time from there
    for submission in subreddit.get_top_from_all(limit = size):
        titleArray = Converter.stringToArray(submission.title)
   
        #trainFile.write("\"")
        #trainFile.write(subreddit_name)
        #trainFile.write("\" ")
        for word in titleArray:
            totalFile.write(word)
            totalFile.write("\n")
            trainFile.write(word)
            trainFile.write(" ")
        trainFile.write("\n")
         
        # Progress Tracker
        if( (count/(size/10)) == math.floor((count/(size/10))) ):
            print ( (100*count/size)," % \tPost #", count)
        count = count + 1
    trainFile.close()
		
totalFile.close()

# Use this java application to lower case, sort, and remove duplicates
os.system('Java RefitDictionary dictionary.txt sortedDictionary.txt')




