package com.logic.util

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL.WithDouble._
/**
  * json工具类
  *
  * @author Wang Guodong wangguodong@richinfo.cn
  *
  */
object JsonUtil extends Serializable{
  private implicit val formats = DefaultFormats

  /**
    * json串转成map
    * @param json 字符串
    * @return map
    */
  def jsonToMap(json:String):Map[String,String]={
    val jValue=parse(json)
    jValue.extract[Map[String,String]]
  }

  /**
    * map 转json串
    * @param jsonMap map
    * @return json串
    */
  def mapToJson(jsonMap:Map[String,String]):String={
    compact(render(jsonMap))
  }
}
