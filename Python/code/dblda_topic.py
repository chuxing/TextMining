#coding:utf-8
import MySQLdb as mdb
import os
import nltk

def Fun_DB(flag,topicid,topic,reposid,msgraw,msg):
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
        if flag == 1:
            sql = "insert into topfile_raw_topic_repos values (%s,%s,%s,%s,%s)"
        elif flag == 2:
            sql = "insert into topfile_tfidf_topic_repos values (%s,%s,%s,%s,%s)"
        elif flag == 3:
            sql = "insert into topfile_raw_topic_repos_long values (%s,%s,%s,%s,%s)"
        else:
            sql = "insert into topfile_tfidf_topic_repos_long values (%s,%s,%s,%s,%s)"
        param = (topicid, topic,reposid,msgraw,msg)
        cursor.execute(sql, param)
        conn.commit()
    except:
        import traceback
        traceback.print_exc()
        conn.rollback()
    finally:
        cursor.close()
        conn.close()
