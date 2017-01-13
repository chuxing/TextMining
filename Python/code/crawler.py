#coding:utf8
import json
import os
import requests
import urllib,urllib2

from itertools import chain
from pathlib import Path
import  bs4

from bs4 import BeautifulSoup
import  dbprocess

def getinfor(url):
    print url
    githubcategory =  url.split("/")[4]
    headers = {'Use-Agent': 'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'}
    req = urllib2.Request(url,headers=headers)
    content = urllib2.urlopen(req).read()
    soup = bs4.BeautifulSoup(content, "html.parser",from_encoding='GB18030')  # BeautifulSoup
    # print soup
    titleall = soup.find_all("li",class_="repo-list-item repo-list-item-with-avatar")
    for titlei in titleall:
        souptitle = bs4.BeautifulSoup(str(titlei),"html.parser")
        title = souptitle.find("h3",class_="mb-1").text.rstrip()
        title = title.replace(" / ","/")
        title = title.replace("\n","")
        try:
            messagelong  = souptitle.find("div",class_="d-inline-block col-9 text-gray pr-4").text
            messagelong = messagelong.replace("\n", "")
            messagelong = messagelong.lstrip().rstrip()
        except:
            continue
        dbprocess.Fun_DB(title,githubcategory,messagelong)