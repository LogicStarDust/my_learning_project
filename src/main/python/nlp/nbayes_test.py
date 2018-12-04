# coding=utf-8
import sys
import os
import numpy as np
from NBayes_lib import *

dataSet,listClasses = loadDataSet()

nb=NBays()
nb.train_set(dataSet,listClasses)
nb.map2vocab(dataSet[0])
print nb.predict(nb.testset)

# 1.通过训练集计算每个词属于每个分类的概率
# 2.通过训练集计算每个类别的概率
# 3.对于测试文本，假设属于某个类别的时候，计算文本所有词属于本类别的概率，再乘以本类别的概率
# 4.取第3步概率最高的那个分类