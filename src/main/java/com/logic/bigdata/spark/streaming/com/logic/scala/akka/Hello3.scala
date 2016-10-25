package com.logic.bigdata.spark.streaming.com.logic.scala.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Hello3 extends App {

  import Greeter._

  val system = ActorSystem("actor-demo-scala")
  val bob: ActorRef = system.actorOf(props("Bob", "How are doing"))
  val alice: ActorRef = system.actorOf(props("alice", "Happy to meet you"))
  bob ! Greet(alice)
  /**
    * 1.建立bob和alice对象
    * 2.给bob对象发送Greet(alice),bob给alice发送askName
    * 3.alice给bob发送TellName（alice）
    * 4.bob输出bob的欢迎词和alice的名字
    */
  //  alice ! Greet(bob)
  Thread.sleep(1000)
  system.shutdown()

  object Greeter {

    case class Greet(peer: ActorRef)

    case object AskName

    case class TellName(name: String)

    def props(name: String, greeting: String) = Props(new Greeter(name, greeting))
  }

  class Greeter(myName: String, greeting: String) extends Actor {

    import Greeter._

    def receive: Receive = {
      case Greet(peer) => peer ! AskName
      case AskName => sender ! TellName(myName)
      case TellName(name) => println(s"$greeting,$name")
    }
  }

}