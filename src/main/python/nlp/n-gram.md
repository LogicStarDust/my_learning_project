# 概念 
> n元语法（英语：n-gram）
>
> 指文本中连续出现的n个语词。n元语法模型是基于(n-1)阶马尔可夫链的一种概率语言模型，通过n个语词出现的概率来推断语句的结构。这一模型被广泛应用于概率论、通信理论、计算语言学（如基于统计的自然语言处理）、计算生物学（如序列分析）、数据压缩等领域。  
当n分别为1、2、3时，又分别称为一元语法（unigram）、二元语法（bigram）与三元语法（trigram）
>> 马尔可夫链（英语：Markov chain）  
>>
> >为状态空间中经过从一个状态到另一个状态的转换的随机过程。该过程要求具备“无记忆”的性质：下一状态的概率分布只能由当前状态决定，在时间序列中它前面的事件均与之无关。这种特定类型的“无记忆性”称作马尔可夫性质。马尔科夫链作为实际过程的统计模型具有许多应用。

# 朴素理解:
*N-gram*,是在文本中滑动一个长度为N的窗口，形成若干的字节片段（gram）。然后统计每个gram的频度，形成文本的gram表，即文本的向量空间，表中每一种gram即一个特征向量纬度。

# 例子
样本：*今天来学习算法*
* unigram:  
今天、来、学习、算法
* bigram:   
今天来、来学习、学习算法
* trigram:  
今天来学习、来学习算法

# 代码实现
这里我们用python实现对文本的n-gram  
注意：这里没有用分词插件，所以把每个字认为一个词，而上面的例子是针对分好词的文本。
<pre>
import string
import operator
import re
import urllib2
# 去除文本中的换行、数字标记、加号
def cleanText(input):
    input=re.sub('\n+'," ",input).lower()
    input=re.sub('\[[0-9]*\]','',input)
    input=re.sub(' +',' ',input)
    input=bytes(input).encode('utf-8')
    return input

# 把文本分割成按字的数组
def cleanInput(input):
    input=cleanText(input)
    cleanInput=[]
    input=input.split(' ')
    
    for item in input:
        item=item.strip(string.punctuation)
        
        if len(item) > 1 or (item.lower()=='a' or item.lower() == 'i'):
            cleanInput.append(item)
    return cleanInput
# 根据n的大小移动窗口，记录gram，并统计频次
def getNgrams(input, n):
    input=cleanInput(input)
    print("input:"+str(input))
    output={}
    for i in range(len(input)-n+1):
        ngramTemp=" ".join(input[i:i+n])
        if ngramTemp not in output:
            output[ngramTemp]=0
        output[ngramTemp] += 1
    return output

# content=urllib2.urlopen(urllib2.Request("https://www.zhihu.com")).read()
content = open("ngram.txt").read()
print(content)

ngrams=getNgrams(content,2)
print("ngrams:"+str(ngrams))
sortedNGrams = sorted(ngrams.items(), key = operator.itemgetter(1), reverse=True) #=True 降序排列
print(sortedNGrams)
</pre>