package com.logic.nga

import java.net.URL

import scala.util.parsing.json.{JSON, JSONArray}


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object Main {


  def main(args: Array[String]): Unit = {

    val url = new URL("http://bbs.bigccq.cn/thread.php?fid=-7&lite=js")
    val con = url.openConnection()
    val data = Array.fill[Byte](1024)(0)
    val nga=new StringBuilder
//    while (in.read(data) > -1) {
//      nga.append(new String(data))
//      println(new String("---"+new String(data)))
//    }
//    println("-----------------\n"+nga)

  }

}
