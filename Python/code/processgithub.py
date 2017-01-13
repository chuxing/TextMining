#coding:utf-8
import MySQLdb as mdb
import os
import nltk
# nltk.download()

file_raw = "word2vector_raw.txt"

config = {
    'host': '127.0.0.1',
    'port': 3306,
    'user': 'root',
    'passwd': '',
    'db': 'github',
    'charset': 'utf8'
}
conn = mdb.connect(**config)
try:
    cursor = conn.cursor()
    cursor.execute('select * from github_use_all')
    results = cursor.fetchall()
    f = open(file_raw,"a+")
    f2 = open("stopwords.txt",'r')
    stopwords = []
    for line in f2.readlines():
        stopwords.append(line[:-1])
    english_punctuations = [',', '.', ':', ';', '?', '(', ')', '[', ']', '!', '@', '#', '%', '$', '*']
    for result in results:
        print result[0]
        strtmp = result[4]
        wordtoken = nltk.tokenize.word_tokenize(strtmp)
        # if len(strtmp.split(' ')) < 11 : continue
        f.write(result[1].split('/')[1] + " ")

        for word in wordtoken:
            wordchange = word.lower()
            if not wordchange in english_punctuations and not wordchange in stopwords:
                f.write(word+" ")
        f.write("\n")
    f.close()
    f2.close()
except:
    import traceback
    traceback.print_exc()
    conn.rollback()
finally:
    cursor.close()
    conn.close()
