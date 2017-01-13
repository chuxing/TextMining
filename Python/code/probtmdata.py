import os
import linecache
import  dbprocesstopic
f = open("E:\wikidata\use-extract\BTM-master\output\\voca.txt")
dictvoca = {}
for line in f.readlines():
    line = line[:-1]
    linesplit = line.split("\t")
    # print  linesplit
    dictvoca[linesplit[0]] = linesplit[1]
# print  dictvoca
f.close()
f1 = open("E:\wikidata\use-extract\BTM-master\output\model\k200.pz_d")
f2 = "E:\wikidata\use-extract\BTM-master\output\model\k200.pw_z"
cntid = 316320
for line in f1.readlines()[316320:]:
    cntid += 1
    print  cntid
    msg = linecache.getline("E:\wikidata\use-extract\BTM-master\sample-data\doc_info_short.txt", cntid)
    print  msg
    dicttopic = {}
    cnt = 0
    line = line[:-2]
    linesplit = line.split(" ")
    for tmpsplit in linesplit:
        cnt += 1
        try:
            dicttopic[cnt] = float(tmpsplit)
        except:
            print "error" + tmpsplit
    dicttopic = sorted(dicttopic.iteritems(),key = lambda d:d[1],reverse = True)
    cnttopicitem = 0
    res = []
    for topicitem in dicttopic:
        tmpres = []
        tmpres.append({"topicpro":topicitem[1]})
        lineword = linecache.getline(f2,topicitem[0])
        dictword = {}
        cntword = 0
        lineword = lineword[:-2]
        linesplit = lineword.split(" ")
        for tmpsplit in linesplit:
            dictword[cntword] = float(tmpsplit)
            cntword+=1
        dictword = sorted(dictword.iteritems(),key = lambda d:d[1],reverse=True)
        for i in dictword[:4]:
            tmpres.append({dictvoca.get(str(i[0])):i[1]})
        res.append(tmpres)
        cnttopicitem+=1
        if cnttopicitem == 4: break
    dbprocesstopic.Fun_DB(cntid,msg,res)