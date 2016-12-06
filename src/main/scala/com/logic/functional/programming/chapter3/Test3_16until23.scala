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
  def filterByFM[A](ml:MyList[A])(f:A=>Boolean):MyList[A]= {
    flatMap(ml)(x => {
      if (f(x)) MyList(x)
      else MyNil
    })
}
  //3.22
  def twoMyListFoldAdd(ml1:MyList[Int],ml2:MyList[Int]):MyList[Int]=ml1 match {
    case MyNil=>MyNil
    case ml if ml2==MyNil=>ml
    case Cons(h,t)=>Cons(h+head(ml2),twoMyListFoldAdd(t,Test3_1until6.tail(ml2)))
  }
  def head[A](ml:MyList[A]):A=ml match {
    case Cons(h,_)=>h
  }

  //3.23
  def zipWith[A,B](ml1:MyList[A],ml2:MyList[B])(f:(A,B)=>A):MyList[A]=ml1 match {
    case MyNil=>MyNil
    case ml if ml2==MyNil=>ml
    case Cons(h,t)=>Cons(f(h,head(ml2)),zipWith(t,Test3_1until6.tail(ml2))(f))
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
    //3.21
    println(filterByFM(ml)(_%2==0))
    //3.22
    println(twoMyListFoldAdd(ml,ml))
    //3.23
    println(zipWith(ml,ml)(_+_))
    println(zipWith(ml,ml)(_*_))
  }
}
