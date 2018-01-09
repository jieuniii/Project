package Spark_JMX
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext
import org.elasticsearch.spark.sql._


object es_test {
  def main(args: Array[String]) {
        
    val conf = new SparkConf().setAppName("jieun-test").setMaster("local")
    
    conf.set("spark.sql.catalogImplementation", "in-memory")
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes.discovery", "true")
    conf.set("es.nodes", "")
    
    val sc = new org.apache.spark.SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    
    val df = sqlContext.read.format("org.elasticsearch.spark.sql").load("scala_jmx")
    df.createTempView("temp")
    sqlContext.sql("SELECT * FROM temp").show()
    
 

  }
}