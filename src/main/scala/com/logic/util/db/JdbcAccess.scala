package com.logic.util.db

import java.sql.{PreparedStatement, ResultSet, Statement, Connection}
import org.slf4j.LoggerFactory

/**
  * 数据存取公共抽象类，直接继承，需要单独处理的orriverd方法即可
  * T泛型=Bean类即可
  * convert:转化为特定的Bean
  * auth:刘宪领
  */
abstract class JdbcAccess[T](conn : Connection) {

  private val log = LoggerFactory.getLogger(classOf[JdbcAccess[T]])

  /**
    * 插入数据
 *
    * @param sql SQL语句
    * @param params 参数列表
    * @param convert 主键转换方法
    * @return 转换结果
    */
   def insert[T] (sql : String, params : Array[Any])(convert : ResultSet => T)  = {
    log.debug("Execute SQL: " + sql)
    val pstmt = conn prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    setParameters(pstmt, params)
    pstmt.executeUpdate
    val rs = pstmt.getGeneratedKeys
    rs.next
    convert(rs)
  }

  /**
    * 更新数据
 *
    * @param sql SQL语句
    * @param params 参数列表
    * @return 影响行数
    */
   def update(sql : String, params : Array[Any]) = createStatement(sql, params).executeUpdate

  /**
    * 查询对象
 *
    * @param sql SQL语句
    * @param params 参数列表
    * @param convert 结果集转换方法
    * @return 泛型对象
    */
   def queryForObject[T] (sql : String, params : Array[Any])(convert : ResultSet => T)  = {
    val rs = query(sql, params)
    if (rs.next) {
      val result = convert(rs)
      if (rs.next) {
        val ex = new ResultsTooManyException
        log.error(ex.getMessage)
        throw ex
      } else Some(result)
    } else None
  }

  /**
    * 查询对象列表
 *
    * @param sql SQL语句
    * @param params 参数列表
    * @param convert 结果集转换方法
    * @return 泛型对象列表
    */
   def queryForList[T] (sql : String, params : Array[Any]) (convert : ResultSet => T) = {
    val rs = query(sql, params)
    var results = List[T]()
    while (rs.next) { results = results :+ convert(rs) }
    results
  }

   
  /**
    * 查询对象映射
 *
    * @param sql SQL语句
    * @param params 参数列表
    * @param convert 结果集转换方法
    * @return 泛型对象映射
    */
   def queryForMap[K, V] (sql : String, params : Array[Any]) (convert : ResultSet => (K, V)) = {
    val rs = query(sql, params)
    var results = Map[K, V]()
    while (rs.next) { results += convert(rs) }
    results
  }

  /**
    * 查询
 *
    * @param sql SQL语句
    * @param params 参数列表
    */
   def query(sql : String, params : Array[Any]) = createStatement(sql, params).executeQuery

  /**
    * 创建声明
 *
    * @param sql SQL语句
    * @param params 参数列表
    */
  private def createStatement(sql : String, params : Array[Any]) = {
    log.debug("Execute SQL: " + sql)
    val pstmt = conn prepareStatement sql
    setParameters(pstmt, params)
    pstmt
  }

  /**
    * 插入参数
 *
    * @param pstmt 预编译声明
    * @param params 参数列表
    */
  private def setParameters(pstmt : PreparedStatement, params : Array[Any]) {
    for (i <- 1 to params.length) { pstmt setObject(i, params(i - 1)) }
  }

}

/**
  * 结果值读取器
  */
object ResultValueGetter {

  /**
    * 查询结果值
 *
    * @param rs 结果集
    * @param getResult 获得单个值结果的方法
    * @return 值
    */
  def getResultValue[T](rs : ResultSet) (getResult : ResultSet => T) = {
    val result = getResult(rs)
    if (rs.wasNull) None else Some(result)
  }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colNum 列号
    */
  def getStringValue(rs : ResultSet, colNum : Int) = getResultValue(rs) { _ getString colNum }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colNum 列号
    */
  def getIntValue(rs : ResultSet, colNum : Int) = getResultValue(rs) { _ getInt colNum }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colNum 列号
    */
  def getLongValue(rs : ResultSet, colNum : Int) = getResultValue(rs) { _ getLong colNum }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colNum 列号
    */
  def getDoubleValue(rs : ResultSet, colNum : Int) = getResultValue(rs) { _ getDouble colNum }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colNum 列号
    */
  def getBooleanValue(rs : ResultSet, colNum : Int) = getResultValue(rs) { _ getBoolean colNum }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colNum 列号
    */
  def getTimestampValue(rs : ResultSet, colNum : Int) = getResultValue(rs) { _ getTimestamp colNum }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colName 列名
    */
  def getStringValue(rs : ResultSet, colName : String) = getResultValue(rs) { _ getString colName }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colName 列名
    */
  def getIntValue(rs : ResultSet, colName : String) = getResultValue(rs) { _ getInt colName }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colName 列名
    */
  def getLongValue(rs : ResultSet, colName : String) = getResultValue(rs) { _ getLong colName }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colName 列名
    */
  def getDoubleValue(rs : ResultSet, colName : String) = getResultValue(rs) { _ getDouble colName }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colName 列名
    */
  def getBooleanValue(rs : ResultSet, colName : String) = getResultValue(rs) { _ getBoolean colName }

  /**
    * 获得字符串结果的值
 *
    * @param rs 结果集
    * @param colName 列名
    */
  def getTimestampValue(rs : ResultSet, colName : String) = getResultValue(rs) { _ getTimestamp colName }

}

/**
  * 结果太多异常
  */
class ResultsTooManyException extends Exception("Returned too many results.") { }