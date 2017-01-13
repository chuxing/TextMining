import re
import nltk
import linecache

rule = re.compile(r'[^a-zA-z]')
fre = {}



def firstprocess():
    f = open("./data/rawfile.txt", "r")
    f2 = open("stopwords.txt", 'r')
    f3 = open("./data/rawfile_xiaoxie.txt","a+")
    stopwords = []
    for line in f2.readlines():
        stopwords.append(line[:-1])
    english_punctuations = [',', '.', ':', ';', '?', '(', ')', '[', ']', '!', '@', '#', '%', '$', '*']
    for line in f.readlines():
        strtmp = str(line)
        wordtoken = nltk.tokenize.word_tokenize(strtmp)
        for word in wordtoken:
            wordchange = word.lower()
            if not wordchange in english_punctuations and not wordchange in stopwords:
                f3.write(wordchange+" ")
        f3.write("\n")
    f.close()
    f2.close()
    f3.close()

def secondprocess():
    f = open("./data/rawfile_xiaoxie.txt", "r")
    f1 = open("./data/rawfile_chart.txt", "a+")

    for line in f.readlines():
        ssplit = line[:-2].split(" ")
        for tmpssplit in ssplit:
            result = rule.sub(' ', tmpssplit)
            f1.write(result + " ")
        f1.write("\n")
    f.close()
    f1.close()
def thirdprocess():
    global fre
    f1 = open("./data/rawfile_chart.txt", "r")
    f3 = open("./data/rawfile_fre.txt", "a+")
    for line in f1.readlines():
        ssplit = line[:-2].split(" ")
        l = len(ssplit)
        print line, l
        if fre.has_key(l):
            fre[l] = fre[l] + 1
        else:
            fre[l] = 1
    fre = sorted(fre.iteritems(), key=lambda d: d[1], reverse=True)
    for item in fre:
        f3.write(str(item[0]) + " " + str(item[1]) + "\n")
    f1.close()
def fourthprocess():
    global fre
    f1 = open("./data/rawfile_chart.txt", "r")
    f2 = open("./data/rawfile_use.txt", "a+")
    f3 = open("./data/rawfile_use_raw.txt","a+")
    f = "./data/rawfile.txt"
    cntline = 0
    for line in f1.readlines():
        cntline += 1
        ssplit = line[:-2].split(" ")
        l = len(ssplit)
        if l >= 20 and l <= 30:
            f2.write(line)
            f3.write(linecache.getline(f,cntline))
    # f.close()
    f1.close()
    f2.close()
    f3.close()

if __name__ == '__main__':
    firstprocess()
    secondprocess()
    thirdprocess()
    fourthprocess()