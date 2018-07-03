
package lutecia.transportation
import lutecia.Lutecia
import lutecia.core.World
import lutecia.network.Network




object SUETransportation {

  /**
    * Static user equilibrium traffic assignement (Wardrop equilibrium)
    *
    * @param world
    * @param tr
    * @return
    */
  def sueAssignement(world: World, tr: SUETransportation): Network = {
    var currentNetwork = world.network
    var currentFlows = currentNetwork.links.map{_.flow}
    for(_ <- 0 until tr.sueIterations){
      currentNetwork = ShortestPathTransportation.shortestPathAssignement(world,tr)
      val epsilon = currentFlows.zip(currentNetwork.links.map{_.flow}).map{case (phi1,phi2)=> math.pow(phi1 - phi2,2)}.sum
      println("SUE Convergence : epsilon = "+epsilon)
    }

    currentNetwork
  }


}



trait SUETransportation extends Transportation {

  def sueIterations: Int = 20


  /**
    *
    * @param world
    * @param lutecia
    * @return
    */
  override def assignTransportation(world: World, lutecia: Lutecia): World = {
    val effectiveNetwork: Network = SUETransportation.sueAssignement(world,this)
    // compute accessibilities
    World(World(world.grid,effectiveNetwork,world.mayors,world.time),lutecia)
  }

}







