package com.logic.scala.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object AkkaTest extends App {

  case class Greeting(greet: String)

  case class Greet(name: String)

  val system = ActorSystem("actor-demo-scala")
  val hello: ActorRef = system.actorOf(Props[Hello])
  hello ! "Bob"
  hello ! Greeting("hello")
  hello ! Greet("Bob")
  hello ! Greet("Wang Guodong")
  hello ! Greeting("fuck")
  hello ! Greet("Bob")
  hello ! Greet("wu")

  Thread.sleep(1000)
  system.shutdown()

  class Hello extends Actor {
    var greeting = ""

    override def receive: Receive = {
      case Greeting(greet) => greeting = greet
      case Greet(name) => println(s"$greeting $name")
    }
  }

}
