# coding=utf-8
from pyspark import SparkContext as sc
from pyspark import SparkConf
from pyspark.ml.linalg import Vectors

conf = SparkConf().setAppName("guodongA").setMaster("local[*]")
sc = sc.getOrCreate(conf=conf)

data = [1, 2, 3, 4, 5]
distData = sc.parallelize(data)
l=distData.count()
print(l)