//import java.io.{BufferedReader, File, FileReader}

import scala.util.Random
import lutecia._
import lutecia.core.World
import lutecia.setup.{EmptyNetwork, ExponentialMixture, GridNetwork, SyntheticSetup}
import lutecia.network._
import lutecia.indicators._

import scala.collection.mutable.ArrayBuffer

object Run extends App{

  val t = System.currentTimeMillis()
  //implicit val rng = new Random

  val model = new Lutecia with SyntheticSetup with ExponentialMixture with GridNetwork {
    //override def worldSize = 30
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




object Test {

  def testGridNetwork(model: Lutecia) = {
    val world = model.initialWorld
  }


  def testNetwork() = {
    Network.testShortestPaths()
  }


}

