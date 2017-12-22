package com.logic.spider

import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.parse

import scalaj.http.Http

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object ManyoubangSpider {
  private implicit val formats = DefaultFormats
  val httpReq = Http("http://www.manyoubang.com/group-showdo-id-36.html")
  val httpMReq = Http("http://www.manyoubang.com/index.php?app=group&ac=comment&ts=fatie")

  def main(args: Array[String]): Unit = {
    println("main begin.")
    for (i <- 36 to 36) {
      for (pageRank <- 1 to 1) {
        getPostFromListPage(pageRank)
      }
    }
  }

  def getValue(jv: JValue)(fieldName: String): String = {
    (jv \ fieldName).extract[String]
  }

  def getPostFromListPage(pageRank: Int) = {
    val list=for(sec<-0 to 1)yield{
      val responseJsonStr =
        httpReq.postForm(Seq(
          "second" -> sec.toString,
          "page" -> pageRank.toString,
          "tagAll" -> "true"
        )).asString
      val jValue = parse(responseJsonStr.body)
      val listJV = jValue \ "list"
      listJV.extract[List[JValue]]
    }

    list.reduce(_++_).foreach(post => {
      val get = getValue(post)(_)
      val authorName = get("authorName")
      val authorId=get("authorId")
      val groupId=get("groupId")
      val postId=get("postId")
      val postTitle = get("postTitle")
      val postCon_a = get("postCon_a")
      val postLink = get("postLink")
      val postGroName = get("postGroName")
      getComment(authorId,postId,groupId)
      println(s"$authorName-$authorId")
    })
  }

  def getComment(userId:String,topicId:String,groupId:String) = {
    val response=httpReq.postForm(Seq(
      "userid" -> userId,
      "topicid" -> topicId,
      "rid" -> topicId,
      "refertypeid" -> "0",
      "tType" -> "11",
      "groupid" -> groupId,
      "commentid" -> ""
    )).asString
    val jValue=parse(response.body)

  }
}
