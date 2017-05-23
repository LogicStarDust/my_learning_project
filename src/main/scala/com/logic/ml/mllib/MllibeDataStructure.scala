package com.logic.ml.mllib

import org.apache.spark.mllib.linalg.Matrices

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object MllibeDataStructure {
  def main(args: Array[String]): Unit = {
    val matrix=Matrices.sparse(3,2,Array(0,1,3),Array(0,2,1),Array(9,6,8))
    println(matrix)
  }

}
