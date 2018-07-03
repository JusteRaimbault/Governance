
package lutecia.transportation

import lutecia.Lutecia
import lutecia.core.World
import lutecia.network.{Link, Network, Path}

import scala.collection.mutable


object ShortestPathTransportation {


  /**
    * Flow assignement using shortest paths (no user feedback)
    *
    * @param world
    * @param tr
    * @return
    */
  def shortestPathAssignement(world: World, tr: ShortestPathTransportation): Network = {
    // compute gravity flows
    val gravityFlows: Map[(Int,Int),Double] = Transportation.gravityFlows(world,tr.iterationsFlows,tr.lambdaFlows)
    //println("Max flow = "+gravityFlows.values.max)
    //println("Min flow = "+gravityFlows.values.min)
    //println(gravityFlows.values)

    // use shortest paths in network to do the assignement
    val paths: Map[(Int,Int),Path] = world.network.paths.map{case ((n1,n2),path)=>((n1.id,n2.id),path)}
    val linkflows: mutable.Map[Link,Double] = new mutable.HashMap[Link,Double]
    world.network.links.foreach{case l => linkflows(l) = 0.0}
    gravityFlows.foreach{case (k,phi) => paths(k).pathLinks.foreach{case l => linkflows(l) = linkflows(l) + phi} }

    // put the flows in the links
    // Q : recompute shortest paths ? new accessibility computed with congestion ?
    //  -> should have both for comparison ? not possible as relocation depend on accessibility !

    // get links with effective speeds
    val effectivelinks = linkflows.map{case (l,phi) => Transportation.flowToEffectiveDistance(l,phi)}.toSeq

    // reconstruct the network with new distances computation
    val networkEffectiveDistances = Network(world.network.nodes,effectivelinks)

    // constructing the world recomputes new accessibilities
    // -> at a higher level
    //World(World(world.grid,networkEffectiveDistances,world.mayors,world.time),lutecia)

    networkEffectiveDistances
  }


}


trait ShortestPathTransportation extends Transportation {

  /**
    * Shortest paths assignement
    * @param world
    * @param lutecia
    * @return
    */
  override def assignTransportation(world: World,lutecia: Lutecia): World = {
    val effectiveNetwork: Network = ShortestPathTransportation.shortestPathAssignement(world,this)
    World(World(world.grid,effectiveNetwork,world.mayors,world.time),lutecia)
  }

}



