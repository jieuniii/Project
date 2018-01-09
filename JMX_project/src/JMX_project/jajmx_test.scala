package JMX_project

import jajmx._

object jajmx_test {
  def main(args: Array[String]) {
    val options = Some(JMXOptions("1.234.25.137", 21003))

    JMX.once(options) { jmx =>
      for (dump <- jmx.threadsDump(0)) {
        val threads = dump.threads
        val countByState = threads.groupBy(_.status).map { case (state, sublist) => state -> sublist.size }.map { case (state, count) => state + ":" + count }.mkString(" ")
        println("Total %d threads, %s".format(threads.size, countByState))
        for (ti <- threads sortBy { _.id }) { println("{\"id\":%d, \"status\":\"%s\", \"name\":\"%s\"}".format(ti.id, ti.status, ti.name, ti.waitedCount)) }
      }
    }

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

/*    JMX.once(options) { jmx =>
      val mythreading = jmx.get("java.lang:type=Threading")
      mythreading.map { _.attributes.foreach { attr => println(attr.name) } }
    }

    JMX.once(options) { jmx =>
      for {
        mbean <- jmx.mbeans
        attr <- mbean.attributes
        value <- mbean.getString(attr)
      } if (mbean.name.toString().startsWith("java")) {
        println(s"${mbean.name} - ${attr.name} = ${value}")
      }
    }

    JMX.once(options) { jmx =>
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