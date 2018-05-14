
package lutecia.setup

import lutecia.core.{Cell, Grid, World}
import lutecia.network.Network

import scala.util.Random

trait SyntheticSetup extends Setup {

  def initialNetwork: Network = SyntheticSetup.emptyNetwork()

}

trait RandomGrid extends SyntheticSetup {

  override def initialWorld: World = World(SyntheticSetup.randomCells(worldSize),initialNetwork)

}


trait ExponentialMixture extends SyntheticSetup {

  /*
  val arrayVals = Array.fill[Cell](size, size) {
    new Cell(0)
  }

  // generate random center positions
  val centers = Array.fill[Int](centersNumber, 2) {
    rng.nextInt(size)
  }

  for (i <- 0 to size - 1; j <- 0 to size - 1) {
    for (c <- 0 to centersNumber - 1) {
      arrayVals(i)(j).population = arrayVals(i)(j).population + maxPopulation * math.exp(-math.sqrt(math.pow((i - centers(c)(0)), 2) + math.pow((j - centers(c)(1)), 2)) / kernelRadius)
    }
  }

  Seq.tabulate(size,size){(i:Int,j:Int)=>new Cell(arrayVals(i)(j).population) }
  */

}


object SyntheticSetup {

  def randomCells(size: Int)(implicit rng: Random): Grid = {
    Grid(Seq.fill(size,size){case i => Cell(i,rng.nextDouble(),rng.nextDouble())})
  }

  def emptyNetwork(): Network = {

  }

}

