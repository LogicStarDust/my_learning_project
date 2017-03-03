package com.logic.util.content

object HBaseContent {
  
  /*内容相似矩阵表 */
  val content_matrix = "content_matrix"
  val content_matrix_f = "cf"
  /*物品相似矩阵表  基于用户-物品协同过滤*/
  val item_matrix = "item_matrix"
  val item_matrix_f = "if"
  /*用户推荐集合 */
  val user_rec_item = "user_rec_item"
  val user_rec_item_f = "uf"
  /*物品相似矩阵表  基于标签-物品协同过滤*/
  val item_matrix_search = "item_matrix_search"
  val item_matrix_search_f = "sf"
  /*Top N 标签推荐物品表 */
  val top_label_rec = "top_label_rec"
  val top_label_rec_f = "tf"
  /* 标签物品推荐表*/
  val lable_item_rec = "lable_item_rec"
  val lable_item_rec_f = "lf"
  /*历史用户搜索词记录表 */
  val user_keyword_collect = "user_keyword_collect"
  val user_keyword_collect_f = "cf"
  
  /*用户推荐取模值*/
  val user_rec_item_delivery:Int = 32
  val item_item_delivery:Int = 4
  val label_rec_item_delivery:Int = 16
  
}