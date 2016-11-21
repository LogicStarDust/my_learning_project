package com.logic.scala.test

/**
  * Created by logic on 2016/10/30.
  */
trait User {
  def name: String
}

class FreeUser(val name: String) extends User
class PremiumUser(val name: String) extends User
object FreeUser{
  def unapply(user: FreeUser): Option[String] =Some(user.name)
}
object PremiumUser{
  def unapply(user: PremiumUser): Option[String] = Some(user.name)
}
