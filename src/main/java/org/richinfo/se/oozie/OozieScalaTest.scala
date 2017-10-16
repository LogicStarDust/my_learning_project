package org.richinfo.se.oozie

import org.apache.oozie.client.OozieClient
import org.apache.oozie.client.WorkflowJob
/**
  * @author Wang Guodong wangguodong@richinfo.cn
  */
object OozieScalaTest {
  def main(args: Array[String]): Unit = {
    println("oozie")
    //获取oozie客户端
    val oc = new OozieClient("http://server104:11000/oozie/")
    //创建配置项，并配置workflows参数
    val conf = oc.createConfiguration()
    conf.setProperty(OozieClient.APP_PATH, "hdfs://foo:8020/usr/tucu/my-wf-app")
    conf.setProperty("jobTracker", "foo:8021")
    conf.setProperty("inputDir", "/usr/tucu/inputdir")
    conf.setProperty("outputDir", "/usr/tucu/outputdir")

    // 提交开始任务
    val jobId = oc.run(conf)
    println("Workflow job submitted")

    // 等待完成
    while (oc.getJobInfo(jobId).getStatus == WorkflowJob.Status.RUNNING) {
      println("Workflow job running ...")
      Thread.sleep(10 * 1000)
    }

    // print the final status o the workflow job
    println("Workflow job completed ...")
    println(wf.getJobInfo(jobId))
  }

}
