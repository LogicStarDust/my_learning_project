import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object DataT {
  def main(args: Array[String]): Unit = {
    val soure=Array("甲,乙","x","OS,windows","windows")
//   val conf=new SparkConf().setAppName("test222")
//    val sc=new SparkContext(conf)
//    val sourceRDD=sc.parallelize(soure)
    val v1=Array(1,2,3)
    val v2=Array(4,5,6)
    val v3=Array(7,8,9)
    val v=Array(v1.toIterable,v2.toIterable,v3.toIterable)
    val vv=multiCartesian[Int](v)(_+_)
    println(vv.size)
    vv.foreach(println)
  }

  def multiCartesian[T](mults:Iterable[Iterable[T]])(f:(T,T)=>T):Iterable[T]={
    if(mults.size==1) mults.flatten
    else cartesian(mults.head,multiCartesian(mults.drop(1))(f))(f)
  }
  def cartesian[T](a1:Iterable[T],a2:Iterable[T])(f:(T,T)=>T): Iterable[T] ={
    a1.flatMap(x1=>
      a2.map(
        x2=> f(x1,x2)
      )
    )
  }

}
