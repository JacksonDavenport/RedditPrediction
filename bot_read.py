import praw
import sys
print ("Collect links from /r/", sys.argv[1])

user_agent = ("j1davenp Learning0.1")
r = praw.Reddit(user_agent = user_agent)

streamFile = open('stream.txt', 'w')
gfyFile = open('gfy.txt', 'w')
totalFile = open('total.txt', 'a')

subreddit = r.get_subreddit(sys.argv[1])

print ("---------------------------------\n")
for submission in subreddit.get_top_from_year(limit = 100):
    title = submission.title.replace(u"\u2019", "'").replace(u"\u2018", "'").replace(u"\u2013", "-").replace(u"\u2014", "-");
    title = title[0:75]
    url = submission.url;
    print ("Title: ", title)
    print ("Score: ", submission.score)
    print ("URL: ", url)
    if "streamable" in url:
        print ("streamable link")
        streamFile.write(url)
        streamFile.write("\n")
        longText = "/r/" + sys.argv[1] + "\t" + title + "\t" + str(submission.score) + "\t" + url
        totalFile.write(longText)
        totalFile.write("\n")
    if "gfycat" in url and "giant" not in url and "fat" not in url:
        print ("gfycat link")
        gfyFile.write(url)
        gfyFile.write("\n")
        longText = "/r/" + sys.argv[1] + "\t" + title + "\t"+ str(submission.score) + "\t" + url
        totalFile.write(longText)
        totalFile.write("\n")
    print ("---------------------------------\n")

streamFile.close()
gfyFile.close()
totalFile.close()

readFile = open('stream.txt', 'r')
print ("Collected:\n")
for line in readFile:
    #webbrowser.open_new_tab(line.rstrip('\n'))
    print ("\t", line.rstrip('\n'))
readFile.close();
readFile = open('gfy.txt', 'r')
for line in readFile:
    #webbrowser.open_new_tab(line.rstrip('\n'))
    print ("\t", line.rstrip('\n'))
readFile.close();	
