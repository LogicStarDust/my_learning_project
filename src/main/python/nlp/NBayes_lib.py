# coding=utf-8
import numpy as np

def loadDataSet():
    postingList = [
        ['我', '爱', '娇娇'],
        ['今天', '天气', '好'],
        ['天下', '第一', '剑客', '李白']
    ]
    classVec = [1, 0, 0]
    return postingList, classVec


class NBays(object):
    def __init__(self):
        self.vocabulary = []    # 词典
        self.idf = 0            # 词典的idf权值向量
        self.tf = 0             # 训练集的权重矩阵
        self.tdm = 0            # P(x|yi)
        self.Pcates = {}        # P(yi)类别词典
        self.labels = []        # 对应每个文本的分类，一个外部导入的列表
        self.doclength = 0      # 训练集文本数
        self.vocablen = 0       # 词典词长
        self.testset = 0        # 测试集合

    def train_set(self, trainset, classVec):
        # 分别记录类别词典和字词词典
        self.cate_prob(classVec)
        self.doclength = len(trainset)
        tempset = set()
        [tempset.add(word) for doc in trainset for word in doc]
        self.vocabulary = list(tempset)
        self.vocablen = len(self.vocabulary)
        self.calc_wordfreq(trainset)    # 计算词频数据集
        self.build_tdm()

    def cate_prob(self, classVec):
        self.labels = classVec
        labeltemps = set(self.labels)
        for labeltemp in labeltemps:
            # print("Pcates labeltemp:",labeltemp,':',float(self.labels.count(labeltemp))/float(len(self.labels)))
            self.Pcates[labeltemp] = float(
                self.labels.count(labeltemp))/float(len(self.labels))

    def calc_wordfreq(self, trainset):
        self.idf = np.zeros([1, self.vocablen])
        self.tf = np.zeros([self.doclength, self.vocablen])
        for indx in xrange(self.doclength):
            for word in trainset[indx]:
                self.tf[indx, self.vocabulary.index(word)] += 1
            for signleword in set(trainset[indx]):
                self.idf[0, self.vocabulary.index(signleword)] += 1

    def build_tdm(self):
        self.tdm = np.zeros([len(self.Pcates), self.vocablen])
        sumlist = np.zeros([len(self.Pcates), 1])
        for indx in xrange(self.doclength):
            self.tdm[self.labels[indx]] += self.tf[indx]
            sumlist[self.labels[indx]] = np.sum(self.tdm[self.labels[indx]])
        self.tdm = self.tdm/sumlist

    def map2vocab(self, testdata):
        self.testset = np.zeros([1, self.vocablen])
        for word in testdata:
            self.testset[0, self.vocabulary.index(word)] += 1

    def predict(self, testset):
        if np.shape(testset)[1] != self.vocablen:
            print "输入错误"
            exit(0)
        predvalue = 0
        predclass = ""
        for tdm_vect, keyclass in zip(self.tdm,self.Pcates):
            temp = np.sum(testset*tdm_vect*self.Pcates[keyclass])
            if temp > predvalue:
                predvalue = temp
                predclass = keyclass
        return predclass

    def calc_tfidf(self, trainset):
        self.idf = np.zeros([1, self.vocablen])
        self.tf = np.zeros([self.doclength, self.vocablen])
        for indx in xrange(self.doclength):
            for word in trainset[indx]:
                self.tf[indx, self.vocabulary.index(word)] += 1
            self.tf[indx] = self.tf[indx]/float(len(trainset[indx]))
            for signleword in set(trainset[indx]):
                self.idf[0, self.vocabulary.index(signleword)] += 1
        self.idf = np.log(float(self.doclength)/self.idf)
        self.tf = np.multiply(self.tf, self.idf)
