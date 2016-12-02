package com.logic.functional.programming.chapter3

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Test3_7until15 {

  def foldRight[A,B](ml:MyList[A],z:B)(f:(A,B)=>B):B=ml match {
    case MyNil=>z
    case Cons(h,t)=>f(h,foldRight(t,z)(f))
  }
}
