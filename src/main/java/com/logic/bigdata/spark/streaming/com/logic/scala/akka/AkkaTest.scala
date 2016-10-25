package com.logic.bigdata.spark.streaming.com.logic.scala.akka

import akka.actor.{Actor, ActorSystem, Props}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object AkkaTest extends App {
  val system = ActorSystem("actor-demo-scala")
  val hello = {
    system.actorOf(Props[Hello])
  }
  hello.!("Bob")
  Thread.sleep(1000)
  system.shutdown()

  class Hello extends Actor {
    override def receive: Receive = {
      case name: String => println(s"Hello $name")
    }
  }

}
