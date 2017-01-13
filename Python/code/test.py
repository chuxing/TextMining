# -*- coding: cp936 -*-
import os
import sys
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
import sys
import string
reload(sys)
import codecs
sys.setdefaultencoding( "utf-8" )
from numpy import *

def tfidf():
    fr = open('./data/topfile_use.txt', "r")
    corpus = []
    for oneline in fr.readlines():
        oneline = oneline[:-2]
        corpus.append(oneline.strip())
    print corpus
    # print  data
    # 将得到的词语转换为词频矩阵
    freWord = CountVectorizer()
    # 统计每个词语的tf-idf权值
    transformer = TfidfTransformer()
    # 计算出tf-idf(第一个fit_transform),并将其转换为tf-idf矩阵(第二个fit_transformer)
    tfidf = transformer.fit_transform(freWord.fit_transform(corpus))
    # 获取词袋模型中的所有词语
    word = freWord.get_feature_names()
    # 得到权重
    weight = tfidf.toarray()
    tfidfDict = {}
    resName = "./tfidf/topfile_Tfidf.txt"
    result = codecs.open(resName, 'w', 'utf-8')
    for i in range(len(weight)):
        print i
        for j in range(len(word)):
            getWord = word[j]
            getValue = weight[i][j]
            result.write(str(weight[i][j]) + ' ')

            if getValue != 0:
                if tfidfDict.has_key(getWord):
                    tfidfDict[getWord] += string.atof(getValue)
                else:
                    tfidfDict.update({getWord: getValue})
        result.write("\n")
    sorted_tfidf = sorted(tfidfDict.iteritems(),
                          key=lambda d: d[1], reverse=True)
    fw = open('./tfidf/topfile_word_rfidf.txt', 'w')
    for i in sorted_tfidf:
        fw.write(i[0] + '\t' + str(i[1]) + '\n')

def kmeans():
    fr = open('./data/rawfile_use.txt', "r")
    corpus = []
    for oneline in fr.readlines():
        oneline = oneline[:-2]
        corpus.append(oneline.strip())
    # 将得到的词语转换为词频矩阵
    freWord = CountVectorizer()
    # 统计每个词语的tf-idf权值
    transformer = TfidfTransformer()
    # 计算出tf-idf(第一个fit_transform),并将其转换为tf-idf矩阵(第二个fit_transformer)
    tfidf = transformer.fit_transform(freWord.fit_transform(corpus))
    # 获取词袋模型中的所有词语
    word = freWord.get_feature_names()
    # 得到权重
    weight = tfidf.toarray()
    print 'Start Kmeans:'
    from sklearn.cluster import KMeans
    clf = KMeans(n_clusters=20)
    s = clf.fit(weight)
    print s

    # 20个中心点
    print(clf.cluster_centers_)

    # 每个样本所属的簇
    print(clf.labels_)
    i = 1
    while i <= len(clf.labels_):
        print i, clf.labels_[i - 1]
        i = i + 1

        # 用来评估簇的个数是否合适，距离越小说明簇分的越好，选取临界点的簇个数
    print(clf.inertia_)
def show():
    print  "f"
def testwordnumber():
    a = set()
    fr = open('./data/topfile_use.txt', "r")
    for line in fr.readlines():
        line = line[:-2].split(" ")
        for tmpi in line:
            if tmpi != "":
                a.add(tmpi)
    print len(a)
    for i in a:
        print i
if __name__ == '__main__':
    testwordnumber()
    # tfidf()
    # kmeans()
    # show()