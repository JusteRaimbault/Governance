package lutecia.setup

import lutecia.Lutecia
import lutecia.core.{Cell, Grid}
import lutecia.governance.Mayor

import scala.util.Random

import org.openmole.spatialdata.utils.math.Statistics

trait SyntheticGrid extends SyntheticSetup {

}




/**
  * Random grid
  */
trait RandomGrid extends SyntheticGrid {

  override def initialGrid: (Grid,Seq[Mayor]) = RandomGrid.randomCells(this)

}

object RandomGrid {

  /**
    * grid with random values
    * @param lutecia
    */
  def randomCells(lutecia: Lutecia): (Grid,Seq[Mayor]) = {
    val rng=lutecia.rng
    val worldSize=lutecia.worldSize
    (Grid(flatCells = Seq.tabulate[Cell](worldSize*worldSize){case i => {val c = Cell(i);Cell(c,(rng.nextDouble(),rng.nextDouble()),"values")}},worldSize),Seq.empty)
  }
}


/**
  * Grid based on exponential mixtures
  */
trait ExponentialMixtureGrid extends SyntheticGrid {

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



  override def initialGrid: (Grid,Seq[Mayor]) = ExponentialMixtureGrid.expMixtureGrid(this)

}


object ExponentialMixtureGrid {
  /**
    *
    * @param worldSize
    * @param centers : Either[Int,Seq[Seq[Int], if Int generates random centers, otherwise coordinates
    * @param maxValue
    * @param kernelRadius
    * @return
    */
  def expMixture(worldSize: Int,centers: Either[Int,Seq[Seq[Int]]], maxValue: Double,kernelRadius: Double,rng: Random): Seq[Seq[(Double,(Int,Int))]] = {
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
  def expMixtureGrid(model: ExponentialMixtureGrid): (Grid,Seq[Mayor]) = {
    val rng = model.rng
    val numberTerritories = model.numberTerritories
    val worldSize = model.worldSize
    val maxValueActives = model.maxValueActives
    val kernelRadiusActives = model.kernelRadiusActives
    val maxValueEmployments = model.maxValueEmployments
    val kernelRadiusEmployments = model.kernelRadiusEmployments

    // coordinates of centers
    val centers = Seq.fill(numberTerritories,2){rng.nextInt(worldSize)}
    // TODO : not close to border and minimal radius between centers

    val activesWithCoords = expMixture(worldSize,Right(centers),maxValueActives,kernelRadiusActives,rng)
    val actives = activesWithCoords.map{case c=>c.map{_._1}}.flatten
    val employmentsWithCoords = expMixture(worldSize,Right(centers),maxValueEmployments,kernelRadiusEmployments,rng)
    val employments = employmentsWithCoords.map{case c=>c.map{_._1}}.flatten
    val coords = activesWithCoords.map{case c=>c.map{_._2}}.flatten
    val flatCells =  actives.zip(employments).zip(coords).zipWithIndex.map{case (((a,e),(i,j)),k)=>{val c = Cell(k,i.toDouble,j.toDouble);Cell(c,(a,e),"values")}}
    val mayors: Seq[Mayor] = centers.map{
      case coords=>{
        val dists = flatCells.map{case c => math.abs(c.x - coords(0))+math.abs(c.y - coords(1))}
        val mind = dists.min
        Mayor(flatCells(dists.indexWhere(_==mind)))
      }
    }
    (Grid(flatCells =flatCells,worldSize),mayors)
  }
}









