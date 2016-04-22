import sys
import os

# Phrase To Test (CHANGE HERE)
test_phrase = "\"kobe bryant ends career with epic game\""

# Validate Input
if(len(sys.argv) < 2):
    print ("Invalid input, please include subreddit name(s)")
    sys.exit()

# Diagnostics
print ("Search through:")
for subreddit_name in (sys.argv[1:]):
    print ("\t/r/", subreddit_name)
	
# Scroll through each subreddit included
for subreddit_name in (sys.argv[1:]):
    command = "Java TitleModeling " + subreddit_name + " " + test_phrase
    os.system(command)


