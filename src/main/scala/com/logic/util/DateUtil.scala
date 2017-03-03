package com.logic.util

import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.{Calendar, Date}

/**
  * 时间工具类
  * auth:刘宪领
  */
object DateUtil {

  val CENTRE_DATE = "yyyy-MM-dd"
  val CENTRE_TIME = "yyyy-MM-dd HH:mm:ss"
  val ONLY_TIME = "HH:mm:ss"
  val NO_CENTRE_DATE = "yyyyMMdd"
  val NO_CENTRE_TIME = "yyyyMMddHHmmss"
  /**
    * 获取当前日期时间
    *
    * @return
    */
  def getNowDate(format: String): String = {
    var now: Date = new Date()
    var dateFormat: SimpleDateFormat = new SimpleDateFormat(format)
    var result = dateFormat.format(now)
    result
  }

  /**
    * 获取前几天日期
    *
    * @param format
    * @param days
    * @return
    */
  def getBeforyDay(format: String, days: Int): String = {
    var dateFormat: SimpleDateFormat = new SimpleDateFormat(format)
    var cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.DATE, -days)
    var result = dateFormat.format(cal.getTime())
    result
  }

  /**
    * 时间戳转化为日期格式
    * @param time
    * @param format
    * @return
    */
  def DateFormat(format:String,time:Long):String={
    var sdf:SimpleDateFormat = new SimpleDateFormat(format)
    var date:String = sdf.format(new Date((time*1000l)))
    date
  }

  /**
    * 计算时间差
    * @param start_time
    * @param end_Time
    * @return
    */
  def getCoreTime(start_time:String,end_Time:String)={
    var df:SimpleDateFormat=new SimpleDateFormat(ONLY_TIME)
    var begin:Date=df.parse(start_time)
    var end:Date = df.parse(end_Time)
    var between:Long=(end.getTime()-begin.getTime())/1000//转化成秒
    var hour:Float=between.toFloat/3600
    var decf:DecimalFormat=new DecimalFormat("#.00")
    decf.format(hour)//格式化

  }

  /**
    * 本月第一天
    * @return
    */
  def getNowMonthStart():String={
    var period:String=""
    var cal:Calendar =Calendar.getInstance();
    var df:SimpleDateFormat = new SimpleDateFormat(CENTRE_DATE);
    cal.set(Calendar.DATE, 1)
    period=df.format(cal.getTime())//本月第一天
    period
  }

  /**
    * 本月最后一天
    * @return
    */
  def getNowMonthEnd():String={
    var period:String=""
    var cal:Calendar =Calendar.getInstance();
    var df:SimpleDateFormat = new SimpleDateFormat(CENTRE_DATE);
    cal.set(Calendar.DATE, 1)
    cal.roll(Calendar.DATE,-1)
    period=df.format(cal.getTime())//本月最后一天
    period
  }

  /**
    * 本周开始时间
    * @return
    */
  def getNowWeekStart():String={
    var period:String=""
    var cal:Calendar =Calendar.getInstance();
    var df:SimpleDateFormat = new SimpleDateFormat(CENTRE_DATE);
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    //获取本周一的日期
    period=df.format(cal.getTime())
    period
  }

  /**
    * 本周最后一天
    * @return
    */
  def getNowWeekEnd():String={
    var period:String=""
    var cal:Calendar =Calendar.getInstance();
    var df:SimpleDateFormat = new SimpleDateFormat(CENTRE_DATE);
    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//这种输出的是上个星期周日的日期，因为老外把周日当成第一天
    cal.add(Calendar.WEEK_OF_YEAR, 1)// 增加一个星期，才是我们中国人的本周日的日期
    period=df.format(cal.getTime())
    period
  }
}