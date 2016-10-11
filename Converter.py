import sys
import os

class Converter:
    # Convert a unicode string into an array of words
	# retain only letters and no single letter words
    @staticmethod
    def stringToArray(value):
        for c in value:
			# [65-90] and [97-122]
            if (ord(c) < 65 or ord(c) > 122 or (ord(c) > 90 and ord(c) < 97)):
                value = value.replace(c, " ")
        value = value.split(' ')
        for word in value:
            if (len(word) < 2):
                value.remove(word)
		
        while('' in value):
            value.remove('')
        
        return value

	# Given a list of the top subreddits from redditlist.com convert it 
	# to a list of subreddits
    @staticmethod
    def makeSubredditList():
        fileName = "Training_Files" + os.sep + "TopSubreddits.txt"
        file = open(fileName, 'r')
        subredditList = []
		
        lineCount = 1
        for line in file:
		    # Skip two lines, read a line
            if lineCount % 3 == 0:
                subredditList.append(line.split()[0])    
            
            lineCount = lineCount + 1
        
        return subredditList

	# Given a list of the top subreddits from the file output it to a file
    @staticmethod
    def printSubredditList():
        readFileName = "Training_Files" + os.sep + "TopSubreddits.txt"
        readFile = open(readFileName, 'r') 	
        writeFileName = "Training_Files" + os.sep + "Subreddits.txt"
        writeFile = open(writeFileName, 'w')
        
        subredditList = []
        lineCount = 1
        for line in readFile:
		    # Skip two lines, read a line
            if lineCount % 3 == 0:
                writeFile.write(line.split()[0] + "\n")
            
            lineCount = lineCount + 1	