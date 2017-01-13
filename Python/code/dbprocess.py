#coding:utf-8
import MySQLdb as mdb
import os
import nltk

def Fun_DB(title,githubcategory,messagelong):
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
        githubcategory = githubcategory.replace("-"," ")
        print  title,githubcategory
        cnt = cursor.execute('select * from github_use_all where FullName = '+ '"' + title +'"')
        if(cnt != 0):
            result = cursor.fetchone()
            cnt2 = cursor.execute('select * from github_use_category where FullName = ' + '"' + title +'"')
            if(cnt2 == 0):
                sql = "insert into github_use_category values (%s,%s,%s,%s,%s,%s,%s,%s)"
                param = (result[0],result[1],result[2],result[3],result[4],result[5],githubcategory,messagelong)
                cursor.execute(sql, param)
            else:
                result2 = cursor.fetchone()
                tmpcategory = result2[6]
                tmpcategory += ","
                tmpcategory += githubcategory
                sql = "update github_use_category set Category = %s where OriginalID = %s"
                param = (tmpcategory,result[0])
                cursor.execute(sql,param)
            conn.commit()
    except:
        import traceback
        traceback.print_exc()
        conn.rollback()
    finally:
        cursor.close()
        conn.close()
