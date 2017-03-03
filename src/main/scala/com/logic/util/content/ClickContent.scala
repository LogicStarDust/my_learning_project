package com.logic.util.content

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
trait ClickContent {
  //spark任务名
  val SPARK_APP_NAME="ClickStreaming"
  //配置文件名
  val CONFIG_NAME = "kafka"
  //配置文件中kafka的brokers名
  val KAFKA_BROKERS_NAME = "kafka.brokers"
  //配置文件中kafka存放搜索数据的topic名
  val KAFKA_SEARCH_DATA_TOPIC_NAME="kafka.topic.searchData"
  //配置文件中kafka属性配置的的前缀，以此前缀为开始的参数，会在去除前缀后最为使用kafka的参数
  val KAFKA_PROPS_PREFIX="kafkapre"
}
