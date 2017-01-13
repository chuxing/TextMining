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
    # ���õ��Ĵ���ת��Ϊ��Ƶ����
    freWord = CountVectorizer()
    # ͳ��ÿ�������tf-idfȨֵ
    transformer = TfidfTransformer()
    # �����tf-idf(��һ��fit_transform),������ת��Ϊtf-idf����(�ڶ���fit_transformer)
    tfidf = transformer.fit_transform(freWord.fit_transform(corpus))
    # ��ȡ�ʴ�ģ���е����д���
    word = freWord.get_feature_names()
    # �õ�Ȩ��
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
    # ���õ��Ĵ���ת��Ϊ��Ƶ����
    freWord = CountVectorizer()
    # ͳ��ÿ�������tf-idfȨֵ
    transformer = TfidfTransformer()
    # �����tf-idf(��һ��fit_transform),������ת��Ϊtf-idf����(�ڶ���fit_transformer)
    tfidf = transformer.fit_transform(freWord.fit_transform(corpus))
    # ��ȡ�ʴ�ģ���е����д���
    word = freWord.get_feature_names()
    # �õ�Ȩ��
    weight = tfidf.toarray()
    print 'Start Kmeans:'
    from sklearn.cluster import KMeans
    clf = KMeans(n_clusters=20)
    s = clf.fit(weight)
    print s

    # 20�����ĵ�
    print(clf.cluster_centers_)

    # ÿ�����������Ĵ�
    print(clf.labels_)
    i = 1
    while i <= len(clf.labels_):
        print i, clf.labels_[i - 1]
        i = i + 1

        # ���������صĸ����Ƿ���ʣ�����ԽС˵���طֵ�Խ�ã�ѡȡ�ٽ��Ĵظ���
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