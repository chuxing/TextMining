import os
import nltk

f = open("extenddata.txt", "r")
f2 = open("extenddata_new.txt","a+")
dict = {}
for line in f.readlines():
    line = line[:-2]
    # print  line
    strsplit = str(line).split(" ")
    for strcmp in strsplit:
        cnt = 0
        if(dict.has_key(strcmp) == True):
            cnt = dict[strcmp]
        dict[strcmp] = cnt + 1
# print dict
f.close()
f = open("extenddata.txt", "r")
for line in f.readlines():
    line = line[:-2]
    # print line
    strsplit = str(line).split(" ")
    cnt = 0
    for strcmp in strsplit:
        if(dict[strcmp] > 3):
            cnt += 1
    if cnt <= 3:continue
    for strcmp in strsplit:
        if(dict[strcmp] > 3):
            f2.write(strcmp+" ")
    f2.write("\n")
f.close()
f2.close()
