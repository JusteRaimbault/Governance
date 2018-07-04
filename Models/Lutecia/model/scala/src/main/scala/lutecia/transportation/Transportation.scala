
package lutecia.transportation

import lutecia.Lutecia
import lutecia.core._
import lutecia.network._
import org.apache.commons.math3.linear._

import scala.collection.mutable
//import scala.collection.mutable._

object Transportation {

  /**
    * Static call to the assignTransportation method
   */
  def assignTransportation(lutecia: Lutecia with Transportation,world: World): World = lutecia.assignTransportation(world,lutecia)



  def emptyTransportation(world: World): World = {
    // no congestion : nothing to do here
    world
  }


  /**
    * Computes theoretical gravity flows between all patches, with a fixed point algorithm.
    *  -> uses effective distance (network distance matrix) : to be iterated in the case of a UserEquilibrium
    *
    * Flows are given by $\phi_{ij} = p_i q_j A_i E_j exp(- lambda_flows d_{ij})$
    *
    * @param world
    * @return
    */
  def gravityFlows(world: World,iterationsFlows: Int = 100, lambdaFlows: Double = 0.05): Map[(Int,Int),Double] = {
    val n = world.grid.cells.flatten.size

    val actives = MatrixUtils.createRealVector(Grid.getValues(world.grid,_.actives))
    val employments = MatrixUtils.createRealVector(Grid.getValues(world.grid,_.employments))
    var p = MatrixUtils.createRealVector(Array.fill(actives.getDimension())(1/actives.getDimension().toDouble))
    var q = MatrixUtils.createRealVector(Array.fill(employments.getDimension())(1/employments.getDimension().toDouble))
    val dmat: RealMatrix = MatrixUtils.createRealMatrix(world.network.distances.map{_.map{case d => math.exp(- d * lambdaFlows)}})
    val ptilde = dmat.operate(employments) map{1 / _}

    /*
    println("Amax = "+employments.toArray().max)
    println("Emax = "+actives.toArray().max)
    println("dmax = "+dmat.getData().map{_.max}.max)
    println("q0 = "+q.getEntry(0))
    println("p0 = "+p.getEntry(0))
    */
    //println(employments.ebeMultiply(q))

    p = ptilde

    for(k <- 0 until iterationsFlows){
      val prevp = p.copy();val prevq = q.copy()

      q = dmat.operate(actives.ebeMultiply(p)) map{1 / _}
      p = dmat.operate(employments.ebeMultiply(q)) map{1 / _}

      val ptot = p.toArray().sum;val qtot = q.toArray().sum
      p = p.mapDivide(ptot)
      q = q.mapDivide(qtot)

      //println("it. "+k)
      //println(p.copy() subtract prevp map math.abs toArray() sum)
      //println(q.copy() subtract prevq map math.abs toArray() sum)
      //  DOES NOT CONVERGE with lambda = 0.05 ? -> OK with renormalization of flows
    }

    //println("p in "+p.toArray().min+" ; "+p.toArray().max)
    //println("q in "+q.toArray().min+" ; "+q.toArray().max)

    val piai = Array.fill(n){p.ebeMultiply(actives).toArray()}
    val qjej = Array.fill(n){p.ebeMultiply(actives).toArray()}.transpose

    dmat.getData.zip(piai).zip(qjej).zipWithIndex.map{
      case (((d,p),q),i) => d.zip(p).zip(q).zipWithIndex.map{case (((x1,x2),x3),j)=> ((i,j),x1*x2*x3)}
    }.flatten.toMap
  }




}


trait Transportation {

  /**
    * Transportation assignement
    *
    * @param world
    * @param lutecia
    * @return
    */
  def assignTransportation(world: World,lutecia: Lutecia): World

  /**
    * iteration for flows algorithms
    * @return
    */
  def iterationsFlows: Int = 20

  /**
    * characteristic distance for flows
    * @return
    */
  def lambdaFlows: Double = 0.05//1.0




  /**
    * Computes effective speeds and corresponding distances, from flows within links.
    *
    * use for example a BPR function
    * Defauts ot nothing
    * @param link
    * @return
    */
  def flowToEffectiveDistance(link: Link,flow: Double): Link = link


  def timeFlowDerivative(link: Link,flow: Double): Double = 0.0


}


trait BPRFlowFunction extends Transportation {

  def bprEpsilon: Double = 0.15
  def bprAlpha: Double = 4

  def bprTime(t0: Double, volume: Double, capacity: Double, epsilon: Double, alpha: Double): Double = {
    t0*(1 + epsilon*math.pow(volume/capacity,alpha))
  }

  /**
    * BPR flow function
    *
    * @param link
    * @param flow
    * @return
    */
  override def flowToEffectiveDistance(link: Link, flow: Double): Link = {
    Link(link.e1,link.e2, link.length, link.speed,
      bprTime(link.length/link.speed,flow,link.capacity,bprEpsilon,bprAlpha),
      link.capacity,
      flow
    )
  }

}




trait EmptyTransportation extends Transportation {

  override def assignTransportation(world: World,lutecia: Lutecia): World = Transportation.emptyTransportation(world)

}






