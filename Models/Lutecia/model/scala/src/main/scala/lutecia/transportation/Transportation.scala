
package lutecia.transportation

import lutecia.Lutecia
import lutecia.core._
import org.apache.commons.math3.linear._

object Transportation {


  def assignTransportation(lutecia: Lutecia with Transportation,world: World): World = lutecia.assignTransportation(world)


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



  /**
    * Computes effective speeds and corresponding distances, from flows within links
    * @param world
    * @return
    */
  def flowsToEffectiveDistances(world: World): World = {

    world
  }


}


trait Transportation {

  def assignTransportation(world: World): World


  def iterationsFlows: Int = 20

  def lambdaFlows: Double = 0.05//1.0



}


trait EmptyTransportation extends Transportation {

  override def assignTransportation(world: World): World = Transportation.emptyTransportation(world)

}



object ShortestPathTransportation {

  def shortestPathFlows(world: World, tr: ShortestPathTransportation): World = {
    // compute gravity flows
    val gravityFlows = Transportation.gravityFlows(world,tr.iterationsFlows,tr.lambdaFlows)
    //println("Max flow = "+gravityFlows.values.max)
    //println("Min flow = "+gravityFlows.values.min)
    //println(gravityFlows.values)

    world
  }

  def shortestPathAssignement(world: World, tr: ShortestPathTransportation): World = {
    shortestPathFlows(world,tr)
  }


}


trait ShortestPathTransportation extends Transportation {

  override def assignTransportation(world: World): World = ShortestPathTransportation.shortestPathAssignement(world,this)

}



