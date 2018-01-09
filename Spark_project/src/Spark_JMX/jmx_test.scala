package Spark_JMX

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import jajmx._

object jmx_test {
  def main(args: Array[String]) {
    
    //val conf = new SparkConf().setAppName("My First Spark App").setMaster("local[2]")
    //var sc = new SparkContext(conf)
    
    val options = Some(JMXOptions("", 21003))
    
    JMX.once(options) { jmx =>
      for (dump <- jmx.threadsDump(0)) {
        val threads = dump.threads
        val countByState = threads.groupBy(_.status).map { case (state, sublist) => state -> sublist.size }.map { case (state, count) => state + ":" + count }.mkString(" ")
        println("Total %d threads, %s".format(threads.size, countByState))
        
        for (ti <- threads sortBy { _.id }) { println("{\"id\":%d, \"status\":\"%s\", \"name\":\"%s\"}".format(ti.id, ti.status, ti.name, ti.waitedCount)) }
      }
    }
    
    
    

  }

}