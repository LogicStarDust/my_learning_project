import math
xArr = [10, 1000, 10000, 20000, 40000, 50000]


def sigmod(x):
    return 1/(math.exp(-x)+1)

def fun1(x):
    x=float(x)
    p2=1/math.log(x)-1.2
    if(p2>0):
        y=1/(x+math.exp(3+math.log(p2)))
    else:
        y=1/(x+math.exp(3+math.log(-p2)))
    return y*100

for i in xArr:
    x1=fun1(i)
    y3=sigmod(x1)
    print("%s\t%s\t%s" % (i,x1, y3))