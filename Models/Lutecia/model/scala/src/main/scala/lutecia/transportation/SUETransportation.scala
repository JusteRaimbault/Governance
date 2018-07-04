
package lutecia.transportation


import lutecia.Lutecia
import lutecia.core.World
import lutecia.network.{Link, Network, Path}

import scala.collection.mutable




object SUETransportation {

  /**
    * Static user equilibrium traffic assignement (Wardrop equilibrium)
    *
    * Implements a path-based algorithm described by
    * Jayakrishnan, R., Tsai, W. T., Prashker, J. N., & Rajadhyaksha, S. (1994). A faster path-based algorithm for traffic assignment.
    *
    * Alternative :
    * Dial, R. B. (2006). A path-based user-equilibrium traffic assignment algorithm that obviates path storage and enumeration. Transportation Research Part B: Methodological, 40(10), 917-936.
    *
    * @param world
    * @param tr
    * @return
    */
  def sueAssignement(world: World, tr: SUETransportation): Network = {

    /**
      * Attribute path flows to links
      * @param p
      * @return
      */
    def computeLinkFlows(p: Map[(Int,Int),Seq[(Path,Double)]]): Map[(Int,Int),Link] = {
      val linkflows: mutable.Map[Link,Double] = new mutable.HashMap[Link,Double]
      world.network.links.foreach{case l => linkflows(l) = 0.0}
      p.foreach {
        case (_, s) =>
          s.foreach { case (p, d) => p.pathLinks.foreach { case l => linkflows(l) = linkflows(l) + d }
          }
      }

      linkflows.map{case (l,phi) => tr.flowToEffectiveDistance(l,phi)}.map{case l => ((l.e1.id,l.e2.id),l)}.toMap
    }

    /**
      * Recompute shortest paths
      * @param links
      * @return
      */
    def computeShortestPaths(links: Seq[Link]): (Map[(Int,Int),Path],Network) = {
      val newNetwork = Network(world.network.nodes,links)
      (newNetwork.paths.map{case ((n1,n2),path)=>((n1.id,n2.id),path)},newNetwork)
    }


    /**
      * reassignate to new paths
      * @param prevpaths
      * @param shortestPaths
      * @return
      */
    def reassignPathFlows(prevpaths: Map[(Int,Int),Seq[(Path,Double)]], shortestPaths: Map[(Int,Int),Path], linkFlows: Map[(Int,Int),Link], flows: Map[(Int,Int),Double]): Map[(Int,Int),Seq[(Path,Double)]] = {

      // update path costs
      val updatedPaths = prevpaths.map{
        case (k,seq) => (k,seq.map{case (p,d) => (p.updateCost(linkFlows),d)})
      }

      val res: mutable.Map[(Int,Int),Seq[(Path,Double)]] = new mutable.HashMap[(Int,Int),Seq[(Path,Double)]]

      shortestPaths.foreach{
          case (k,shortest) => {
            val existing = updatedPaths(k)
            var newPaths = existing
            if (!existing.map{case (p,d) => p.cost}.contains(shortest.cost)) {
              val tailNewPaths = existing.map{
                case (p,d) => {
                  // here change the flow of this new path
                  // note : directed network
                  val p1set = p.pathLinks.map{case l => (l.e1.id,l.e2.id)}.toSet
                  val p2set = shortest.pathLinks.map{case l => (l.e1.id,l.e2.id)}.toSet
                  val noncommonlinks: Seq[(Int,Int)]= ((p1set &~ p2set) union (p2set &~ p1set)).toSeq
                  val cumderiv = noncommonlinks.map{case (i1,i2) => tr.timeFlowDerivative(linkFlows((i1,i2)),linkFlows((i1,i2)).flow)}.sum
                  (p,d - (p.cost - shortest.cost) / cumderiv)
                }
              }
              val totalFlows = tailNewPaths.map{_._2}.sum
              newPaths = Seq((shortest, flows(k) - totalFlows)) ++ tailNewPaths
            }

            res(k) = newPaths
          }

        }

      res.keys.zip(res.values).toMap
    }


    // compute gravity flows
    val gravityFlows: Map[(Int,Int),Double] = Transportation.gravityFlows(world,tr.iterationsFlows,tr.lambdaFlows)


    val initialshortest: Map[(Int,Int),Path] = world.network.paths.map{case ((n1,n2),p) => ((n1.id,n2.id),p)}

    // all paths - to simplify, shortest is always the first one in the Seq
    var paths = new mutable.HashMap[(Int,Int),(Path,Set[(Path,Double)])]
    // initialisation
    gravityFlows.foreach{case (k,d) => paths(k) = Seq((initialshortest(k),d))}

    var effectivelinks = computeLinkFlows(paths)
    var shortestpaths = computeShortestPaths(effectivelinks.values.toSeq)._1

    for (it <- 0 until tr.sueIterations) {
      println("SUE Iteration "+it)

      // reassign flows
      paths = reassignPathFlows(paths, shortestpaths, effectivelinks, gravityFlows)

      // compute link flows
      effectivelinks = computeLinkFlows(paths)

      // compute new shortest paths
      shortestpaths = computeShortestPaths(effectivelinks.values.toSeq)._1

      // FIXME add an endogenous convergence criteria ?
    }

    shortestpaths._2
  }


}



trait SUETransportation extends Transportation {

  def sueIterations: Int = 200


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







