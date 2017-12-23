package com.logic.spider

import java.io.{File, PrintWriter}

import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.parse

import scalaj.http.{Http, HttpRequest}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object ManyoubangSpider {
  private implicit val formats = DefaultFormats
  val httpMReq = Http("http://www.manyoubang.com/index.php?app=group&ac=comment&ts=fatie")
  def main(args: Array[String]): Unit = {
    println("main begin.")
    for (groupid <- 1 to 999) {
      try{
        val httpReq = Http(s"http://www.manyoubang.com/group-showdo-id-$groupid.html")
        val data = getPostFromListPage(httpReq)
        val groupName=data.head._1
        println(s"$groupid,$groupName,post num=${data.size}")
        val writer = new PrintWriter(new File(s"${groupid}_$groupName.csv"),"gbk") //当前工程根目录下
        data.map(line=>{
          val lintStr={line.productIterator.toList.mkString("\",\"")}
          "\""+lintStr+"\""
        }).foreach(writer.println)
        writer.close()
      }catch {
        case e:Throwable=>{
          println(s"$groupid,error!error:$e")
        }
      }
    }
  }

  def getValue(jv: JValue)(fieldName: String): String = {
    (jv \ fieldName).extract[String]
  }

  def getPostFromListPage(httpReq: HttpRequest): List[(String, String, String, String, String, String, String, String, String, String, String, String, String)] = {
    var is_lastPage = 1
    val pageList=for(page<- 1 to 100 if is_lastPage==1 ) yield {
      val list = for (sec <- 0 to 1) yield {
        val responseJsonStr =
          httpReq.postForm(Seq(
            "second" -> sec.toString,
            "page" -> page.toString,
            "tagAll" -> "true"
          )).asString
        val jValue = parse(responseJsonStr.body)
        is_lastPage =try{
          (jValue \ "is_lastPage").extract[Int]
        }catch {
          case t:Throwable=> 0
        }
        val listJV = jValue \ "list"
        listJV.extract[List[JValue]]
      }

      list.reduce(_ ++ _).flatMap(post => {
        val get = getValue(post)(_)
        val authorName = get("authorName")
        val authorId = get("authorId")
        val groupId = get("groupId")
        val postId = get("postId")
        val postTitle = get("postTitle")
        val postCon_a = get("postCon_a")
        val postTime = get("postTime")
        val commentNum = get("CommentNum")
        val hugNum = get("hugNum")
        val repostNum = get("repostNum")
        val postLink = get("postLink")
        val postGroName = get("postGroName")
        if (commentNum != "null" && commentNum!=null) {
          getComment(authorId, postId, groupId)
            .map(comment => {
              //帖子id 发帖者	帖子标题	帖子正文	发帖时间	收藏数(无)
              // 	转发数	评论数	拥抱数	评论者	评论内容
              // 评论时间	备注
              (postGroName, postId, authorName, postTitle, postCon_a, postTime,
                repostNum, commentNum, hugNum, comment._1, comment._2,
                comment._3, comment._4)
            })
        } else {
          //发帖者	帖子标题	帖子正文	发帖时间	收藏数
          // 	转发数	评论数	拥抱数	评论者	评论内容
          // 评论时间	备注
          Iterable((postGroName, postId, authorName, postTitle, postCon_a, postTime,
            repostNum, commentNum, hugNum, "null", "null",
            "null", "null"))
        }
      })
    }
    pageList.reduce(_++_)
  }

  def getComment(userId: String, topicId: String, groupId: String): List[(String, String, String, String)] = {
    val response = httpMReq.postForm(Seq(
      "userid" -> userId,
      "topicid" -> topicId,
      "rid" -> topicId,
      "refertypeid" -> "0",
      "tType" -> "11",
      "groupid" -> groupId,
      "commentid" -> ""
    )).asString
    val jValue = parse(response.body)
    val commentList = (jValue \ "list").extract[List[JValue]]
    commentList.map(comment => {
      val orgJValue = comment \ "org"
      val get = getValue(comment)(_)
      val quick_author = get("quick_author")
      val quick_con = get("quick_con")
      val quick_time = get("quick_time")
      val orguick_author = (orgJValue \ "quick_author").extractOrElse("null")
      (quick_author, quick_con, quick_time, orguick_author)
    })
  }
}
