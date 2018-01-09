package JMX_project

import jajmx._
import java.io._

object jmx {
  def main(args: Array[String]) {
    val options = Some(JMXOptions("1.234.25.137", 21003))
    //val pw = new FileWriter("detail_threads.json", true)

    val s1 = "{"
    val s3 = ", "
    val s6 = "}"
    val s7 = "\""
    val s8 = "\n"

    JMX.once(options) { jmx =>
      for (dump <- jmx.threadsDump(0)) {
        val threads = dump.threads
        val countByState = threads.groupBy(_.status).map { case (state, sublist) => state -> sublist.size }.map { case (state, count) => state + ":" + count }.mkString(" ")
        println("Total %d threads, %s".format(threads.size, countByState))
        for (ti <- threads sortBy { _.id }) {
          /*println("{\"types\":\"threads_list\", \"id\":%d, \"status\":\"%s\", \"name\":\"%s\"}".format(ti.id, ti.status, ti.name))*/
          var text = s1.concat("\"types\":\"threads_list\", ").concat("\"id\":").concat(ti.id.toString).concat(s3).concat("\"status\":").concat(s7).concat(ti.status).concat(s7).concat(s3).concat("\"name\":").concat(s7).concat(ti.name).concat(s7).concat(s6).concat(s8)
          print(text)
          //pw.write(text)
        }
      }
    }

    JMX.once(options) { jmx =>
      for {
        mbean <- jmx.mbeans
        attr <- mbean.attributes
        value <- mbean.getString(attr)
      } if (!attr.name.toString().startsWith("License") && !mbean.name.toString.contains("\"")) {
        /*println(s"${mbean.name} - ${attr.name} = ${value}")*/
        /*println("{\"types\":\"mbean_list\", " + "\"Mbean_name\":\"" + s"${mbean.name}" + "\", " + "\"Attr_name\":\"" + s"${attr.name}" + "\", " + "\"value\":\"" + s"${value}" + "\"}")*/
        var text2 = s1.concat("\"types\":\"mbean_list\", ").concat("\"Mbean_name\":\"").concat(s"${mbean.name}").concat("\", ").concat("\"Attr_name\":\"").concat(s"${attr.name}").concat("\", ").concat("\"value\":\"").concat(s"${value}").concat("\"}").concat(s8)
        print(text2)
        //pw.write(text2)
      }
    }

    //pw.close()

    /*
    JMX.once(options) { jmx =>
      val java_home = jmx.javaHome
      val java_runtime_version = jmx.javaRuntimeVersion
      val java_spec_version = jmx.javaSpecificationVersion
      val java_version = jmx.javaVersion
      val os_name = jmx.osName
      val os_version = jmx.osVersion
      val sys_proper = jmx.systemProperties
      val user_home = jmx.userHome
      val user_name = jmx.userName
      println(java_home, java_runtime_version, java_spec_version, java_version, os_name, os_version, sys_proper, user_home, user_name)
    }
*/

    /*    JMX.once(options) { jmx =>
      val mythreading = jmx.get("java.lang:type=Threading")
      mythreading.map {
        _.attributes.foreach { attr => println(attr.name) }
      }
    }*/
    /*
    JMX.once(options) { jmx =>
      for {
        mbean <- jmx.mbeans
        attr <- mbean.attributes
        value <- mbean.getString(attr)
      } if (mbean.name.toString().startsWith("java")) {
        /*println(s"${mbean.name} - ${attr.name} = ${value}")*/
        println("{\"Mbean_name\":\"" + s"${mbean.name}" + "\", " + "\"Attr_name\":\"" + s"${attr.name}" + "\", " + "\"value\":\"" + s"${value}" + "\"}")

      }
    }*/

    /*    JMX.once(options) { jmx =>
      for {
        mbean <- jmx.mbeans
        attr <- mbean.attributes
        value <- mbean.getString(attr)
      } {
        println(s"${mbean.name} - ${attr.name} = ${value}")
      }
    }*/

  }
}