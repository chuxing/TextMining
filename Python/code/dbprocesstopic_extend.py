#coding:utf-8
import MySQLdb as mdb
import os
import nltk

def Fun_DB(id,msg,res):
    config = {
        'host': '127.0.0.1',
        'port': 3306,
        'user': 'root',
        'passwd': '',
        'db': 'github-wikipedia',
        'charset': 'utf8'
    }
    conn = mdb.connect(**config)
    dict = {}

    for tmpres in res:
        base = tmpres[0].values()[0]
        # print base
        for idx,val in enumerate(tmpres):
            if idx == 0:continue
            name = val.keys()[0]
            pre = val.values()[0]
            if dict.has_key(name) == True:
                dict[name] += base * pre
            else:
                dict[name] = base * pre
    dict = sorted(dict.iteritems(),key = lambda d:d[1],reverse=True)
    try:
        cursor = conn.cursor()
        sql = "insert into btmtopic_extend values (%s,%s,%s,%s,%s,%s,%s)"
        param = (id, msg, str(res[0]), str(res[1]), str(res[2]), str(res[3]),str(dict))
        cursor.execute(sql, param)
        conn.commit()
    except:
        import traceback
        traceback.print_exc()
        conn.rollback()
    finally:
        cursor.close()
        conn.close()
