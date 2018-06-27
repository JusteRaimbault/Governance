package lutecia

import java.io.FileWriter

package object utils {

  def exportCSV[T](data: Array[Array[T]],file: String,header: Option[String] = None,separator: String = ";"): Unit = {
    val writer : FileWriter = new FileWriter(file)
    if(header.isDefined) writer.write(header.get+"\n")
    println("Exporting to csv data of length "+data.length)
    var i = 0
    data.foreach{
      r =>
        //val s = r.mkString(separator)
        var s = ""
        r.foreach(d => s+=";"+d.toString)
        writer.write(s+"\n")
        i+=1
    }
    println(i)

    writer.close()
  }

}
