package com.logic.functional.programming.chapter3

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test3_16until23 {
  //3.16
  def mlAddOne(ml:MyList[Int]):MyList[Int]=ml match {
    case MyNil=>MyNil
    case Cons(h,t)=>Cons(h+1,mlAddOne(t))
  }
  //3.17
  def mlToString[A](ml:MyList[A]):MyList[String]=ml match {
    case MyNil=>MyNil
    case Cons(h,t)=>Cons(h.toString,mlToString(t))
  }
  //3.18
  def map[A,B](ml:MyList[A])(f:A=>B):MyList[B]=ml match {
    case MyNil=>MyNil
    case Cons(h,t)=>Cons(f(h),map(t)(f))
  }
  //3.19
  def filter[A](ml:MyList[A])(f:A=>Boolean):MyList[A]=ml match {
    case MyNil=>MyNil
    case Cons(h,t) if f(h)=>Cons(h,filter(t)(f))
    case Cons(h,t) if !f(h)=>filter(t)(f)
  }
  //3.20
  def flatMap[A,B](ml:MyList[A])(f:A=>MyList[B]):MyList[B]={
    Test3_7until15.appendMLS(map(ml)(f))
  }
  //3.21
  def filterByFM[A](ml:MyList[A])(f:A=>Boolean):MyList[A]={
    flatMap(ml)
  }
  def main(args: Array[String]): Unit = {
   val ml=MyList(1,2,3,4,5)
    //3.16
    println(mlAddOne(ml))
    //3.17
    println(mlToString(ml))
    //3.18
    println(map(ml)(_+3))
    //3.19
    println(filter(ml)(_%2==0))
    //3.20
    println(flatMap(MyList(1,2,3))(x=>MyList(x,x)))
    //2.21

  }
}
