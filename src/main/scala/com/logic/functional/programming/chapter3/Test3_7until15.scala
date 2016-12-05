package com.logic.functional.programming.chapter3

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test3_7until15 {
  //3.9
  def length[A](as: MyList[A]): Int = {
    foldRight(as, 0)((_, y) => 1 + y)
  }

  def foldRight[A, B](ml: MyList[A], z: B)(f: (A, B) => B): B = ml match {
    case MyNil => z
    case Cons(h, t) => f(h, foldRight(t, z)(f))
  }

  //3.10
  @annotation.tailrec
  def foldLeft[A, B](ml: MyList[A], z: B)(f: (A, B) => B): B = ml match {
    case MyNil => z
    case Cons(h, t) => foldLeft(t, f(h, z))(f)
  }

  //3.12
  def reverse[A](ml: MyList[A]): MyList[A] = {
    foldLeft(ml, MyNil: MyList[A])(Cons(_, _))
  }

  //3.13
  def foldRight2[A, B](ml: MyList[A], z: B)(f: (A, B) => B): B = {
    foldLeft(reverse(ml), z)(f)
  }

  //3.14
  def append[A](ml: MyList[A], z: A): MyList[A] = {
    foldRight(ml, MyList(z))(Cons(_, _))
  }

  //3.15
  def appendMLS[A](mls: MyList[MyList[A]]): MyList[A] = {
    foldRight(mls, MyNil: MyList[A])(
      (x: MyList[A], y: MyList[A]) => {
        foldRight(x, y)(Cons(_, _))
      }
    )
  }

  def main(args: Array[String]): Unit = {
    //3.7
    val re = foldRight(MyList(1.2, 3.4, 4.5, 0.0, 1.1), 0.0)(_ * _)
    println(re)
    //3.8
    val l = foldRight(MyList(1, 2, 3, 4), MyNil: MyList[Int])(Cons(_, _))
    println(l)
    //3.9
    val myList = MyList(1, 2, 3, 4)
    println(length(myList))
    //3.11
    println(foldLeft(myList, 0)(_ + _))
    println(foldLeft(myList, 1)(_ * _))
    println(foldLeft(myList, 0)((x, _) => x + 1))
    //3.12
    println(reverse(myList))
    //3.13
    println(foldRight2(myList, 0)(_ + _))
    //3.14
    println(append(myList, 9))
    //3.15
    val myList2 = MyList(5, 6, 7, 8)
    val myList3 = MyList(15, 16, 17, 18)
    val ml = MyList(myList, myList2, myList3)
    println(appendMLS(ml))
  }
}
