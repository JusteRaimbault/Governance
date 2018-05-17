
package lutecia.setup

import lutecia.core.{Cell, Grid, World}
import lutecia.network.{Link, Network, Node}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Synthetic setup
  */
trait SyntheticSetup extends Setup {

  def initialGrid: Grid

  /**
    * The network will generally depends on the grid
    * @param grid
    * @return
    */
  def initialNetwork(grid: Grid): Network

  override def initialWorld: World = {
    val grid = initialGrid
    World(World(grid,initialNetwork(grid),0),this)
  }

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
  def expMixture(worldSize: Int,centers: Either[Int,Seq[Seq[Int]]], maxValue: Double,kernelRadius: Double): Seq[Seq[(Double,(Int,Int))]] = {
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
    Seq.tabulate(worldSize,worldSize){(i:Int,j:Int)=>(vals(i)(j),(i,j))}
  }

  /**
    * Grid from exponential mixture with fixed number of centers
    */
  def expMixtureGrid(): Grid = {
    // coordinates of centers
    val centers = Seq.fill(numberTerritories,2){rng.nextInt(worldSize)}
    // TODO : not close to border and minimal radius between centers

    val activesWithCoords = expMixture(worldSize,Right(centers),maxValueActives,kernelRadiusActives)
    val actives = activesWithCoords.map{case c=>c.map{_._1}}.flatten
    val employmentsWithCoords = expMixture(worldSize,Right(centers),maxValueEmployments,kernelRadiusEmployments)
    val employments = employmentsWithCoords.map{case c=>c.map{_._1}}.flatten
    val coords = activesWithCoords.map{case c=>c.map{_._2}}.flatten
    Grid(flatCells = actives.zip(employments).zip(coords).zipWithIndex.map{case (((a,e),(i,j)),k)=>{val c = Cell(k,i.toDouble,j.toDouble);Cell(c,(a,e),"values")}},worldSize)
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

  override def initialNetwork(grid: Grid): Network = emptyNetwork()

}


/**
  * Grid network
  */
trait GridNetwork extends SyntheticSetup {


  /**
    *
    * @param grid
    * @return
    */
  def gridNetwork(grid: Grid): Network = {
    // create nodes
    val nodes: Seq[Seq[Node]] = grid.cells.map{case r => r.map{case c => Node(c)}}
    // create edges
    val edges = ArrayBuffer[Link]()
    //dirty
    for (i <- 0 to nodes.size - 1 ; j <- 0 to nodes(0).size - 1) {
      if(i-1>0){if(j-1>0){edges.append(Link(nodes(i)(j),nodes(i-1)(j-1),1/euclidianPace))};edges.append(Link(nodes(i)(j),nodes(i-1)(j),1/euclidianPace));if(j+1<nodes(0).size){edges.append(Link(nodes(i)(j),nodes(i-1)(j+1),1/euclidianPace))}}
      if(j-1>0){edges.append(Link(nodes(i)(j),nodes(i)(j-1),1/euclidianPace))};if(j+1<nodes(0).size){edges.append(Link(nodes(i)(j),nodes(i)(j+1),1/euclidianPace))}
      if(i+1<nodes.size){if(j-1>0){edges.append(Link(nodes(i)(j),nodes(i+1)(j-1),1/euclidianPace))};edges.append(Link(nodes(i)(j),nodes(i+1)(j),1/euclidianPace));if(j+1<nodes(0).size){edges.append(Link(nodes(i)(j),nodes(i+1)(j+1),1/euclidianPace))}}
    }
    Network(nodes.flatten,edges)
  }

  override def initialNetwork(grid: Grid): Network = gridNetwork(grid)

}



/**
  * Slime mould network
  */
trait SlimeMouldNetwork extends SyntheticSetup {



}





