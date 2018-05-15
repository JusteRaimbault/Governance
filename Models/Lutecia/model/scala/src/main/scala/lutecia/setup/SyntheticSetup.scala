
package lutecia.setup

import lutecia.core.{Cell, Grid, World}
import lutecia.network.Network

import scala.util.Random

/**
  * Synthetic setup
  */
trait SyntheticSetup extends Setup {

  def initialNetwork: Network

  def initialGrid: Grid

  override def initialWorld: World = World(initialGrid,initialNetwork)

}



object SyntheticSetup {

}


/**
  * Random grid
  */
trait RandomGrid extends SyntheticSetup {

  /**
    * grid with random values
    * @param size
    */
  def randomCells(size: Int): Grid = {
    Grid(flatCells = Seq.tabulate[Cell](size*size){case i => {val c = Cell(i);Cell(c,(rng.nextDouble(),rng.nextDouble()),"values")}},worldSize)
  }

  override def initialGrid: Grid = randomCells(worldSize)

}


trait ExponentialMixture extends SyntheticSetup {

  /**
    * Parameters
    */
  // number of centers -> Lutecia's number of territories

  /**
    * number of actives at the center of the kernel
    */
  def maxValueActives: Double = 500.0

  /**
    * number of employments at the center of the kernel
    */
  def maxValueEmployments: Double = 500.0

  /**
    * size of exponential kernels
    */
  def kernelRadiusActives: Double = 1.0
  def kernelRadiusEmployments: Double = 0.8

  /**
    *
    * @param worldSize
    * @param centers : Either[Int,Seq[Seq[Int], if Int generates random centers, otherwise coordinates
    * @param maxValue
    * @param kernelRadius
    * @return
    */
  def expMixture(worldSize: Int,centers: Either[Int,Seq[Seq[Int]]], maxValue: Double,kernelRadius: Double): Seq[Double] = {
    //val vals = Seq.fill(worldSize,worldSize)(0.0)
    val vals = Array.fill(worldSize,worldSize)(0.0)
    val coords = centers match {
      case Left(i) => Seq.fill(i,2){rng.nextInt(worldSize)}
      case Right(c) => c
    }
    for(i<- 0 to worldSize-1; j<- 0 to worldSize-1){
      for(c <- coords){
        vals(i)(j) = vals(i)(j) + maxValue*math.exp(-math.sqrt(math.pow((i - c(0)), 2) + math.pow((j - c(1)), 2)) / kernelRadius)
      }
    }
    //array to seq
    Seq.tabulate(worldSize,worldSize){(i:Int,j:Int)=>vals(i)(j)}.flatten
  }

  /**
    * Grid from exponential mixture with fixed number of centers
    */
  def expMixtureGrid(): Grid = {
    // coordinates of centers
    val centers = Seq.fill(numberTerritories,2){rng.nextInt(worldSize)}
    // TODO : not close to border and minimal radius between centers

    val actives = expMixture(worldSize,Right(centers),maxValueActives,kernelRadiusActives)
    val employments = expMixture(worldSize,Right(centers),maxValueEmployments,kernelRadiusEmployments)
    Grid(flatCells = actives.zip(employments).zipWithIndex.map{case ((a,e),i)=>{val c = Cell(i);Cell(c,(a,e),"values")}},worldSize)
  }

  override def initialGrid: Grid = expMixtureGrid

}


/**
  * Network setups
  */


/**
  * Empty network
  */
trait EmptyNetwork extends SyntheticSetup {

  /**
    * Empty network
    * @return
    */
  def emptyNetwork(): Network = Network(Seq.empty,Seq.empty)

  override def initialNetwork: Network = emptyNetwork()

}


/**
  * Slime mould network
  */
trait SlimeMouldNetwork extends SyntheticSetup {



}





