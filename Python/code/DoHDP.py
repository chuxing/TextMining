import codecs
from gensim.models import LdaModel
from gensim.corpora import Dictionary
import gensim
import dblda_repos
import dblda_topic
import json
import  linecache
from gensim.models import  HdpModel

stopwords = codecs.open('stopwords.txt', 'r', encoding='utf8').readlines()
stopwords = [w.strip() for w in stopwords]


topicnumer = 50

def process_data(flag):
    train = []
    if(flag == 1):
        fp = codecs.open('./data/rawfile_use.txt', 'r', encoding='utf8')
    else:
        fp = codecs.open('./data/topfile_use.txt', 'r', encoding='utf8')
    for line in fp:
        print line
        line = line.split()
        train.append([w for w in line if w not in stopwords and w != ""])

    dictionary = gensim.corpora.Dictionary(train)
    if(flag == 1):
        dictionary.save('./HDP/long/hdpfile.dict')
    else:
        dictionary.save('./HDP/short/hdpfile.dict')
    # print dictionary
    corpus = [dictionary.doc2bow(text) for text in train]
    if(flag == 1):
        gensim.corpora.MmCorpus.serialize('./HDP/long/hdpfile.mm', corpus)
    else:
        gensim.corpora.MmCorpus.serialize('./HDP/short/hdpfile.mm', corpus)

def train_lda(flag):
    if(flag == 1):
        dictionary = gensim.corpora.Dictionary.load('./HDP/long/hdpfile.dict')
        corpus = gensim.corpora.MmCorpus('./HDP/long/hdpfile.mm')
    else:
        dictionary = gensim.corpora.Dictionary.load('./HDP/short/hdpfile.dict')
        corpus = gensim.corpora.MmCorpus('./HDP/short/hdpfile.mm')
    tfidf = gensim.models.TfidfModel(corpus)
    corpus_tfidf = tfidf[corpus]
    hdp_raw = HdpModel(corpus=corpus, id2word=dictionary)
    if(flag == 1):
        hdp_raw.save('./HDP/long/hdp_raw.model')
    else:
        hdp_raw.save('./HDP/short/hdp_raw.model')

    hdp_tfidf = HdpModel(corpus_tfidf, id2word=dictionary)
    if(flag == 1):
        hdp_tfidf.save('./HDP/long/hdp_tfidf.model')
    else:
        hdp_tfidf.save('./HDP/short/hdp_tfidf.model')


def test_lda(flag):
    if flag == 1 :
        ldaraw_model = gensim.models.ldamodel.LdaModel.load('./LDA/short/lda_raw.model')
    elif flag == 2:
        ldaraw_model = gensim.models.ldamodel.LdaModel.load('./LDA/short/lda_tfidf.model')
    elif flag == 3:
        ldaraw_model = gensim.models.ldamodel.LdaModel.load('./LDA/long/lda_raw.model')
    else:
        ldaraw_model = gensim.models.ldamodel.LdaModel.load('./LDA/long/lda_tfidf.model')
    # ldatfidf_model = gensim.models.ldamodel.LdaModel.load('./LDA/short/lda_tfidf.model')
    dictionary = gensim.corpora.Dictionary.load('./LDA/short/ldatopfile.dict')
    # corpus = gensim.corpora.MmCorpus('./LDA/short/ldatopfile.mm')
    if flag == 1 or flag == 2:
        f = open("./data/topfile_use.txt","r")
        f1 = "./data/topfile_use_raw.txt"
    else:
        f = open("./data/rawfile_use.txt","r")
        f1 = "./data/rawfile_use_raw.txt"
    dict_repos = {}
    dict_repos_raw = {}
    dict_topic = [[]for i in range(topicnumer)]
    reposid = 0
    for line in f.readlines():
        reposid += 1
        # print reposid,line
        rawline = linecache.getline(f1,reposid)
        dict_repos[reposid] = line[:-2]
        dict_repos_raw[reposid] = rawline[:-2]
        text = line.split()
        # text = [word for word in line if word not in stopwords]
        # print text
        bow_vector = dictionary.doc2bow(text)
        topic_score = {}
        tmpcnt = 0
        indextop = 0
        scoretop = 0
        mostrelatedtopic = ""
        for index, score in sorted(ldaraw_model[bow_vector], key=lambda tup: -1 * tup[1]):
            tmpcnt += 1
            if tmpcnt == 1:
                indextop = index
                scoretop =score
                mostrelatedtopic = ldaraw_model.print_topic(index)
            # dblda_repos.Fun_DB(flag,reposid,line,index,score,ldaraw_model.print_topic(index))
                dict_topic[index].append(reposid)
            topic_score[index] = score
            # break
            # print "topic: {}\t Score: {}\t Topic: {}".format(index, score, ldaraw_model.print_topic(index))
        topic_score = sorted(topic_score.iteritems(),key = lambda d:d[1],reverse = True)

        dblda_repos.Fun_DB(flag,reposid,rawline,line,indextop,scoretop,mostrelatedtopic,json.dumps(topic_score))
    topicid = 0
    for reposidlist in dict_topic:
        msg = []
        rawmsg = []
        # print  topicid
        for id in reposidlist:
            msg.append(dict_repos[id])
            rawmsg.append(dict_repos_raw[id])
        # print reposidlist,msg
        json_id = json.dumps(reposidlist)
        json_rawmsg = json.dumps(rawmsg)
        json_msg = json.dumps(msg)
        # print json_id,json_msg
        dblda_topic.Fun_DB(flag,topicid,ldaraw_model.print_topic(topicid),json_id,json_rawmsg,json_msg)
        topicid += 1
    f.close()
    # f1.close()
def test():
    hdp_short_raw_model = gensim.models.HdpModel.load('./HDP/short/hdp_raw.model')
    hdp_short_tfidf_model = gensim.models.HdpModel.load('./HDP/short/hdp_tfidf.model')
    hdp_long_raw_model = gensim.models.HdpModel.load('./HDP/long/hdp_raw.model')
    hdp_long_tfidf_model = gensim.models.HdpModel.load('./HDP/long/hdp_tfidf.model')
    # print hdp_long_tfidf_model.m_K
    print hdp_short_raw_model.print_topics(-1, 5)
    print hdp_short_tfidf_model.print_topics(20, 5)
    print hdp_long_raw_model.print_topics(20, 5)
    print hdp_long_tfidf_model.print_topics(20, 5)


if __name__ == '__main__':
    # process_data(1)
    # train_lda(1)
    # process_data(2)
    # train_lda(2)
    test()
    # test_lda(1)
    # test_lda(2)
    # test_lda(3)
    # test_lda(4)