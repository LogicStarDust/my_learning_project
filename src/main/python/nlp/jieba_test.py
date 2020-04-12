# coding=utf-8

import jieba
# import jieba.posseg as pseg
# reload(sys)
# sys.setdefaultencoding('utf8')

sent = '艾泽拉斯国家地理是一个魔兽世界玩家聚集的论坛'
# print jieba
wordlist = jieba.cut(sent, cut_all=True)
for e in wordlist:
    print(e)
# pseglist = pseg.cut(sent)
# for e in pseglist:
#     print(e)
