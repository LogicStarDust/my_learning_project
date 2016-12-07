package com.logic.functional.programming.chapter3

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test3_24until29 {
  //3.24
  def hasSubsequence[A](sup: MyList[A], sub: MyList[A]): Boolean = {
    def comp[A](sup: MyList[A], sub: MyList[A]): Boolean = sup match {
      //当母列表遍历完，发现子列表也结束即为查找到了，否则没找到
      case MyNil => if (sub == MyNil) true else false
      //当发现子列表为空，直接返回true
      case _ if sub == MyNil => true
      //如果发现本节点和子列表的节点相同，去母列表剩下的部分去找子列表剩下的部分
      case Cons(h, t) => {
        if (h == Test3_16until23.head(sub))
          comp(t, Test3_1until6.tail(sub))
        else
          false
      }
    }

    sup match {
      //如果发现本节点和子列表的节点相同，去母列表剩下的部分去找子列表剩下的部分
      case Cons(h, t) => {
        if (h == Test3_16until23.head(sub))
          comp(t, Test3_1until6.tail(sub))
        else
          hasSubsequence(t, sub)
      }
    }
  }


  def main(args: Array[String]): Unit = {
    val ml1 = MyList(1, 2, 3, 7, 8, 4, 5, 6)
    val ml2 = MyList(7, 9)
    //3.24
    println(hasSubsequence(ml1, ml2))
    //3.25
    val a = Leaf(1)
    val b = Branch(a, a)
    val c = Branch(b, b)
    println(c.size)
    //3.26
    val l1=Leaf(1)
    val l2=Leaf(5)
    val l3=Leaf(3)
    val l4=Leaf(4)
    val root=Branch(Branch(l1,l2),Branch(l3,l4))
    println(root.maximum)
    //3.27
    val root1=Branch(Branch(Branch(l2,l3),l1),l4)
    println(root1.depth)
    //3.28
    println(root1)
    println(root1.map(_+1))
    //3.29
    println(root1.fold(Leaf(_):MyTree[Int])(Branch(_,_)))

  }
}
