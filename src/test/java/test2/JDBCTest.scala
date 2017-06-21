package test2

import java.sql.{DriverManager, PreparedStatement}


/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object JDBCTest {
  def main(args: Array[String]): Unit = {
//    val topDao=new TopNDao[(String,String)]()
//    val a=topDao.queryForMap(topDao.topNData,Array())
//    println(a)
//    val i=new ItemDao()
//    val re=i.query(i.queryNewItem,Array("2014-03-11"))


//    val conn=JdbcPool.getConn()
//    val re=conn.createStatement().executeQuery("select goods_id from t_gc_common_goods where substring(create_time,1,10)=\"2014-03-11\"")
//    println(re.getRow)

    Class.forName("com.mysql.jdbc.Driver")
    val conn = DriverManager.getConnection("jdbc:mysql://192.168.1.105:3306/ml_ex_ceshi?user=ceshi&password=ceshi")
    val re=conn.createStatement().executeQuery("select goods_id from t_gc_common_goods where substring(create_time,1,10)=\"2014-03-11\"")
    println(re.getRow)

  }

}
