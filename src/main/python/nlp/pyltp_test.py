# coding=utf-8

import sys
import os
from pyltp import *

reload(sys)
sys.setdefaultencoding('utf8')

print("start")
model_path = 'D:\guodong5\ltp_data_v3.4.0\cws.model'

segmentor = Segmentor()
segmentor.load(model_path)
words = segmentor.segment("我的键盘是机械键盘,艾泽拉斯国家地理是一个论坛")

print "segment re:"+"|".join(words)

postagger = Postagger()
postagger.load('D:\guodong5\ltp_data_v3.4.0\pos.model')
postags = postagger.postag(words)

for w, pt in zip(words, postags):
    print("%s\t%s" % (pt, w))

print("------------")

recognizer = NamedEntityRecognizer()
recognizer.load('D:\guodong5\ltp_data_v3.4.0\\ner.model')
netags = recognizer.recognize(words, postags)

for w, postag, netag in zip(words, postags, netags):
    print("%s\t%s\t%s" % (postag, netag, w))


