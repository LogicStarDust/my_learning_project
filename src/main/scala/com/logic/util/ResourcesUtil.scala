package com.logic.util

import java.util.ResourceBundle

/**
  * 配置文件读取
  * user:刘宪领
  */
object ResourcesUtil {
  private val config = ResourceBundle.getBundle("config")

  def getVal(key:String): String ={
    var value = config.getString(key)
    // 去除多余的空格
    if (value != null)
      value = value.trim()
    value

}


}
