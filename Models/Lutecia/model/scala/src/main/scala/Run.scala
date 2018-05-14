//import java.io.{BufferedReader, File, FileReader}

import scala.util.Random

import lutecia._

object Run extends App{

  val t = System.currentTimeMillis()

  implicit val rng = new Random

  val model = new Lutecia {
    override def worldSize: Int = 20
    override def lambda: Double = 0.05
    override def alpha: Double = 0.1
    override def gammaA: Double = 0.9
    override def gammaE: Double = 0.8


  }

  println("Ellapsed Time : "+(System.currentTimeMillis()-t)/1000.0)

}