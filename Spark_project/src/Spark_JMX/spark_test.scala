package Spark_JMX

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf


object spark_test {
  def main(args: Array[String]): Unit = {
/*    val testFile = "doc/test.txt"*/
    val conf = new SparkConf().setAppName("My First Spark App").setMaster("local[2]")
    var sc = new SparkContext(conf)
/*    val testData = sc.textFile(testFile, 2).cache()

    println(testData.count())*/

/*    val json1 = """{"reason" : "business", "airport" : "SFO"}"""
    val json2 = """{"participants" : 5, "airport" : "OTP"}"""

    val rdd = sc.makeRDD(Seq(json1, json2))
    rdd.take(10).foreach(println)*/    
 
    val date = new java.sql.Timestamp(System.currentTimeMillis())
    println(date)
    
  }
}