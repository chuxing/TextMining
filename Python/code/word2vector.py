import gensim, logging
# coding:utf-8
import sys
import gzip
import bz2file
import logging,gensim,os
reload(sys)
sys.setdefaultencoding( "utf-8" )

from gensim.models.word2vec import LineSentence
import logging
import os.path
import sys
import multiprocessing

from gensim.corpora import WikiCorpus
from gensim.models import Word2Vec
from gensim.models.word2vec import LineSentence


f = open("")

class TextLoader(object):
    def __init__(self,dirname):
        self.dirname = dirname

    def __iter__(self):
        for fname in os.listdir(self.dirname):
            fname = os.path.join(self.dirname, fname)
            print fname
            input = open(fname,'r')
            # if fname.endswith("txt"):
            #     input = open(fname,'r')
            # else:
            #     input = bz2file.open(fname,'r')
            line = str(input.readline())
            while line!=None and len(line) > 4:
                # print line
                segments = line.split(' ')
                yield  segments
                line = str(input.readline())

if __name__ == '__main__':
    # program = os.path.basename('python')
    # logger = logging.getLogger(program)
    # logging.basicConfig(format='%(asctime)s: %(levelname)s: %(message)s')
    # logging.root.setLevel(level=logging.INFO)
    # logger.info("running %s" % ' '.join(sys.argv))
    #
    # sentences = TextLoader('E:\\wikidata\\use-extract\\trainingdata\\')
    # model = gensim.models.Word2Vec(sentences, size=300, window=5, min_count=5, workers=multiprocessing.cpu_count())
    # model.save('word2vector_addwiki.model')
    # model.save_word2vec_format('wiki.en.text.vector', binary=False)
    # model = gensim.models.Word2Vec.load('word2vector.model')
    model = gensim.models.Word2Vec.load("./word2vector/wiki.en.text.vector")
    for line in f.readlines():
        print 'adsf'


print 'ok'
