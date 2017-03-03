package com.logic.util.content

/**
  * config配置文件
  * user:刘宪领
  */
object ConfigContent {

  val ALL_BEHAVER_HDFS_URL = "all_behaver_hdfs_url"
  val CONTENT_BEHAVER_HDFS_URL = "content_behaver_hdfs_url"
  /**搜索行为数据*/
  val SEARCH_TAGS_BEHAVER_HDFS_URL = "search_tags_behaver_hdfs_url"
  
  val DRIVER = "jdbc.driver"
  val USERNAME = "jdbc.username"
  val PASSWORD = "jdbc.password"
  val JDBC_URL = "jdbc.url"
  var MAX_CONNECTION= "jdbc.max_connection"
  var CONNECTION = "jdbc.connection"
  /**基于内容的指标K*/
  val CONTENT_K = "content_k"
  /**协同过滤指标K*/
  val SYNERGY_K = "synergy_k"
  /**用户喜好指标K*/
  val USER_LIKE_K = "user_like_k"

  /**标签最热词前K个*/
  val TAGS_TOP_K = "tags_top_k"
  /**标签指标K*/
  val TAGS_REC_K = "tags_rec_k"

  val HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum"
  val ZOOKEEPER_PORT ="hbase.zookeeper.property.clientPort"

  //clickStreaming的检查点目录
  val CLICK_STREAMING_CHECK_POINT="click_streaming_check_point"
  //clickStreaming的批处理间隔
  val CLICK_STREAMING_BITCH_DURATION="click_streaming_bitch_Duration"
  //把cookie和username映射成唯一标识的接口
  val USER_ID_API="userIdAPI"

  val item_index = "item_index"

  //日志清洗时候，目标日志包含的特征字段
  val CLICK_STREAMING_LOG_CLEAN_LOG_TAG="click_streaming_log_clean_log_tag"
}
