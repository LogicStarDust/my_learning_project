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