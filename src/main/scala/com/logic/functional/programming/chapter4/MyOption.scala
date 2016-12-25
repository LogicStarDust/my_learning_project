package com.logic.functional.programming.chapter4

/**
  * Created by logic on 2016/12/24.
  */
sealed trait MyOption[+A] {
  def map[B](f: A => B): MyOption[B] = this match {
    case MyNone => MyNone
    case MySome(x) => MySome(f(x))
  }

  def flatMap[B](f: A => MyOption[B]): MyOption[B] = this match {
    case MyNone => MyNone
    case MySome(x) => f(x)
  }

  def getOrElse[B >: A](default: => B): B = this match {
    case MyNone => default
    case MySome(x) => x
  }

  def orElse[B >: A](ob: => MyOption[B]): MyOption[B] = this match {
    case MyNone => ob
    case MySome(x) => MySome(x)
  }

  def filter(f: A => Boolean): MyOption[A] = this match {
    case MyNone => MyNone
    case MySome(x) => if (f(x)) MySome(x) else MyNone
  }
}

case class MySome[+A](get: A) extends MyOption[A]

case object MyNone extends MyOption[Nothing]
