# coding=utf-8

import sys
import os
import re
import nltk
from nltk.tree import Tree  # nltk tree 结构
from nltk.grammar import DependencyGrammar  # 导入依存 句法包
from nltk.parse import *
from pyltp import *

reload(sys)
sys.setdefaultencoding('utf8')

words = "中华人民共和国 的 首都 是 北京".split(" ")

postagger = Postagger()
postagger.load('D:\guodong5\ltp_data_v3.4.0\pos.model')
postags = postagger.postag(words)
for w, pt in zip(words, postags):
    print("%s\t%s" % (pt, w))
print("---------------")
parser = Parser()
parser.load('D:\guodong5\ltp_data_v3.4.0\parser.model')
arcs = parser.parse(words, postags)
arclen = len(arcs)
conll = ''
for i in xrange(arclen):
    if arcs[i].head == 0:
        arcs[i].relation = 'ROOT'
    conll += "\t%s(%s)\t%s\t%s\t%s\n" % (words[i], postags[i],
                                         postags[i], str(arcs[i].head), arcs[i].relation)

print conll
conlltree=DependencyGraph(conll)
tree=conlltree.tree()
tree.draw()