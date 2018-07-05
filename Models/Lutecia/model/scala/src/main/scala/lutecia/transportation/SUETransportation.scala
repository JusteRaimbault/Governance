
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
    def computeLinkFlows(p: Map[(Int,Int),Seq[(Path,Double)]],links: Map[(Int,Int),Link]): Map[(Int,Int),Link] = {
      val linkflows: mutable.Map[(Int,Int),Double] = new mutable.HashMap[(Int,Int),Double]
      links.values.foreach{case l => linkflows((l.e1.id,l.e2.id)) = 0.0}
      p.values.foreach {
        _.foreach { case (p, d) => p.pathLinks.foreach { case l => linkflows((l.e1.id,l.e2.id)) = linkflows((l.e1.id,l.e2.id)) + d }
          }
      }

      //println(linkflows.size)

      val totalpathflows = p.values.map{_.map{case (path,flow) => flow * path.pathLinks.size}.sum}.sum
      println("path flows = "+totalpathflows)
      println("link flows = "+linkflows.map{_._2}.sum)
      assert(math.abs(linkflows.map{_._2}.sum - totalpathflows)<1e-8,"Non conservation of flows in link flows computation")

      linkflows.map{case (k,phi) => tr.flowToEffectiveDistance(links(k),phi)}.map{case l => ((l.e1.id,l.e2.id),l)}.toMap
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

      //println(prevpaths.values.map{case s => s.map{case (p,d)=>d}.sum}.sum)

      // update path costs
      val updatedPaths = prevpaths.map{
        case (k,seq) => (k,seq.map{case (p,d) => {val updatedpath =p.updateCost(linkFlows); (updatedpath,updatedpath.cost)}})
      }

      //println(updatedPaths.values.map{case s => s.map{case (p,d)=>d}.sum}.sum)

      val res: mutable.Map[(Int,Int),Seq[(Path,Double)]] = new mutable.HashMap[(Int,Int),Seq[(Path,Double)]]

      shortestPaths.foreach{
          case (k,shortest) => {
            val existing = updatedPaths(k)
            var newPaths = existing
            //if (!existing.map{case (p,d) => p.cost}.contains(shortest.cost)) {
            if (shortest.cost < existing.map{_._1.cost}.min) {
              val tailNewPaths = existing.map{
                case (p,d) => {
                  // here change the flow of this new path
                  // note : directed network
                  val p1set = p.pathLinks.map{case l => (l.e1.id,l.e2.id)}.toSet
                  val p2set = shortest.pathLinks.map{case l => (l.e1.id,l.e2.id)}.toSet
                  val noncommonlinks: Seq[(Int,Int)]= ((p1set &~ p2set) union (p2set &~ p1set)).toSeq
                  val cumderiv = noncommonlinks.map{case (i1,i2) => tr.timeFlowDerivative(linkFlows((i1,i2)),linkFlows((i1,i2)).flow)}.sum
                  if (cumderiv == 0.0) {
                    //println("cost diff = " + (p.cost - shortest.cost))
                    //println(p.pathLinks)
                    //println(shortest.pathLinks)
                    //println((p.pathLinks.size,d));
                    //println("noncommonlinks : "+noncommonlinks.size)}
                  }
                  (p, if (cumderiv != 0.0) math.max(0.0,d - (p.cost - shortest.cost) / cumderiv) else d)
                }
              }
              val totalFlows = tailNewPaths.map{_._2}.sum
              //println("Total redirected = "+totalFlows)
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
    val pathsmut = new mutable.HashMap[(Int,Int),Seq[(Path,Double)]]
    // initialisation
    gravityFlows.foreach{case (k,d) => pathsmut(k) = Seq((initialshortest(k),d))}

    // loaded paths
    var paths = pathsmut.keys.zip(pathsmut.values).toMap

    // loaded links
    var links = world.network.links.map{case l => ((l.e1.id,l.e2.id),l)}.toMap
    //println("|Links| = "+links.size)
    // load flows to link
    //var effectivelinks = computeLinkFlows(paths,links)
    var effectivelinks: Map[(Int,Int),Link] = Map.empty
    //println("eff links"+effectivelinks.size)
    // loaded shortest paths
    //var (shortestpaths,network) = computeShortestPaths(effectivelinks.values.toSeq)
    var shortestpaths: Map[(Int,Int),Path] = Map.empty
    var network: Network = Network.empty
    // initial shortest paths did not take into account loaded network ?

    for (it <- 0 until tr.sueIterations) {
      println("SUE Iteration "+it)

      // compute link flows
      effectivelinks = computeLinkFlows(paths,links)
      println("Effective links : "+effectivelinks.size)

      // compute new shortest paths
      val res = computeShortestPaths(effectivelinks.values.toSeq)
      shortestpaths = res._1
      network = res._2

      links = network.links.map{case l => ((l.e1.id,l.e2.id),l)}.toMap

      // reassign flows
      paths = reassignPathFlows(paths, shortestpaths, effectivelinks, gravityFlows)
      println("Total flow = "+paths.values.map{case s => s.map{case (p,d) => d}.sum}.sum)


      // FIXME add an endogenous convergence criteria ?
    }

    network
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







