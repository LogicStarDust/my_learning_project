package com.logic.functional.programming.chapter3

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
sealed trait MyTree[+A] {
  def size: Int = this match {
    case Leaf(_) => 1
    case Branch(l, r) => 1 + l.size + r.size
  }

  def maximum: Int = this match {
    case Leaf(v) => Integer.parseInt(v.toString)
    case Branch(l, r) => if (l.maximum > r.maximum) l.maximum else r.maximum
  }

  def depth: Int = this match {
    case Leaf(_) => 1
    case Branch(l, r) => if (l.depth > r.depth) l.depth+1 else r.depth+1
  }
  def map[B](f:A=>B):MyTree[B]=this match {
    case Leaf(v) =>Leaf(f(v))
    case Branch(l, r) =>Branch(l.map(f),r.map(f))
  }
  def fold[B](f1:A=>B)(f:(B,B)=>B):B=this match {
    case Leaf(v)=>f1(v)
    case Branch(l,r)=>f(l.fold(f1)(f),r.fold(f1)(f))
  }
}

case class Leaf[A](value: A) extends MyTree[A]

case class Branch[A](left: MyTree[A], right: MyTree[A]) extends MyTree[A]
