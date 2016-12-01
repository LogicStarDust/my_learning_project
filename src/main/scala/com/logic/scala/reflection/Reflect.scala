package com.logic.scala.reflection


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Reflect {
  def main(args: Array[String]): Unit = {

    //加载类
    val cls=getClass.getClassLoader.loadClass("com.logic.functional.programming.chapter1.Cafe")
    //实例对象
    val aa=cls.newInstance()
    //获取str方法
    val str=cls.getMethod("str")
    //在aa对象上调用aaa方法
    println(str.invoke(aa))
  }
}
