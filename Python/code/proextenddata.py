#coding:utf-8
import MySQLdb as mdb
import os
import nltk
import json
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

f = open("extenddata.txt", "a+")
f2 = open("stopwords.txt",'r')
stopwords = []
for line in f2.readlines():
    stopwords.append(line[:-1])
english_punctuations = [',', '.', ':', ';', '?', '(', ')', '[', ']', '!', '@', '#', '%', '$', '*']

def Fun_DB():
    config = {
        'host': '127.0.0.1',
        'port': 3306,
        'user': 'root',
        'passwd': '',
        'db': 'github-wikipedia',
        'charset': 'utf8'
    }
    conn = mdb.connect(**config)
    try:
        cursor = conn.cursor()
        sql = "select * from exactpage"
        cursor.execute(sql)
        results = cursor.fetchall()
        return  results
    except:
        import traceback
        traceback.print_exc()
        conn.rollback()
    finally:
        cursor.close()
        conn.close()

def process(usedata):
    words = usedata.values()
    for word in words:
        for i in word:
            if i is None: continue
            wordchange = i.lower()
            if wordchange not in english_punctuations and wordchange not in stopwords:
                f.write(str(wordchange) + " ")

def dowrite(raw,id):
    if len(raw[id]) == 0:
        return
    jsonData = json.loads(raw[id])
    if id == 2:
        process(jsonData.values()[0])
    else:
        for content in jsonData:
            process(content.values()[0])


if __name__ == '__main__':
    results = Fun_DB()
    cnt = 0

    for raw in results:
        cnt += 1
        f.write(raw[1] + " ")
        strtmp = raw[3]
        wordtoken = nltk.tokenize.word_tokenize(strtmp)
        for word in wordtoken:
            wordchange = word.lower()
            if not wordchange in english_punctuations and not wordchange in stopwords:
                f.write(wordchange+" ")

        # f.write(raw[3] + " ")
        dowrite(raw,2)
        dowrite(raw,4)
        f.write("\n")
        print cnt
    f.close()