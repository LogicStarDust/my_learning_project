package com.logic.functional.programming.chapter1

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
class Cafe {

  def buyCoffee(cc: String): (Coffee, Charge) = {
    val cup = new Coffee()
    (cup, Charge(cc, cup.price))
  }

  def buyCoffees(cc: String, n: Int): (List[Coffee], Charge) = {
    val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffee(cc))
    val (coffees, charges) = purchases.unzip
    (coffees, charges.reduce((c1, c2) => c1.combine(c2)))
  }

}

class Coffee {
  val price = 21.5
  override def toString={
    "java"
  }
}

case class Charge(cc: String, amount: Double) {
  def combine(other: Charge): Charge = {
    if (cc == other.cc) {
      Charge(cc, amount + other.amount)
    } else {
      throw new Exception("不能合并的消费")
    }
  }
}
object Main{
  def main(args: Array[String]): Unit = {
    val cafe=new Cafe
    val re=cafe.buyCoffees("6574",7)
    println(re._1+"--"+re._2)
  }
}
