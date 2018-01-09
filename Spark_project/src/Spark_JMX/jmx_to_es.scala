package Spark_JMX
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import jajmx._
import org.elasticsearch._
import org.apache.spark.sql.functions._
import org.elasticsearch.spark.sql._
import org.apache.spark.sql.{ Dataset, DataFrame, SparkSession }

object jmx_to_es {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("JIEUN-APP").setMaster("local[2]")
    conf.set("spark.sql.catalogImplementation", "in-memory")
    conf.set("es.index.auto.create", "true")
    conf.set("es.nodes.discovery", "true")
    conf.set("es.nodes", "")

    val sc = new org.apache.spark.SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val options = Some(JMXOptions("", 21003))

    
    //-------------------------- # Threads list # --------------------------
    JMX.once(options) { jmx =>
      for (dump <- jmx.threadsDump(0)) {
        val threads = dump.threads

        val sqlContext = new org.apache.spark.sql.SQLContext(sc)
        import sqlContext.implicits._

        val threads_list_temp = threads.toDF()
        threads_list_temp.createTempView("threads_temp")
        val threads_list = sqlContext.sql("SELECT NOW() as log_time, name, id, status, stack  FROM threads_temp").toDF()

        threads_list.saveToEs("spark_jmx_threads/jmx")
 
      }
      println("1. Threads Elasticsearch Save")

    }
    
    //-------------------------- # Mbeans list # --------------------------
    val mbeans = JMX.once(options) { jmx =>
      for { 
        mbean <- jmx.mbeans()
        attr <- mbean.attributes()
        values <- mbean.getString(attr);
        
        if (!attr.name.toString().startsWith("License") && !mbean.name.toString.contains("\""))
      } yield (mbean.name, attr.name, values)
    }

    val sqlContext2 = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext2.implicits._

    val newNames = Seq("mbean_name", "attr_name", "value")
    val mbean_list_temp = mbeans.toDF(newNames: _*)

    mbean_list_temp.createTempView("mbeans_temp")
    val mbean_list = sqlContext.sql("SELECT NOW() as log_time, mbean_name, attr_name, value FROM mbeans_temp").toDF()
    mbean_list.saveToEs("spark_jmx_mbeans/jmx")
    
    println("2. Mbeans Elasticsearch Save")
    

    
/*    ------------------<TEST>----------------------
    val ttest = JMX.once(options) { jmx =>
      for {
          mem <- jmx.memory();
          heap_data <- mem.getNumberComposite("HeapMemoryUsage")
          non_heap_data <- mem.getNumberComposite("NonHeapMemoryUsage")
          (name, value) <- data
      } yield (heap_data, non_heap_data) 
    }
     -----------------------------------------------*/
      
      
  
  }
}

  