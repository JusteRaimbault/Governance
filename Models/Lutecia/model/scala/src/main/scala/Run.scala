//import java.io.{BufferedReader, File, FileReader}

import scala.util.Random
import lutecia._
import lutecia.core.World
import lutecia.setup._
import lutecia.network._
import lutecia.indicators._

import scala.collection.mutable.ArrayBuffer

object Run extends App{

  val t = System.currentTimeMillis()
  //implicit val rng = new Random

  val model = new Lutecia with SyntheticSetup with ExponentialMixtureGrid with GridNetwork {
    //override def worldSize = 30
    override def finalTime: Int = 10
    //define parameters
    override def lambda: Double = 0.05
    override def beta: Double = 1.8
    override def alpha: Double = 0.1
    override def gammaA: Double = 0.9
    override def gammaE: Double = 0.8

    //override def world: World = initialWorld
  }


  //implicit val lutecia: Lutecia = model

  //Test.testNetwork()
  //Test.testGridNetwork(model)

  val result: Result = RunModel.run(model)
  //Indicators.computeIndicators(result)

  println("Ellapsed Time : "+(System.currentTimeMillis()-t)/1000.0)

}





