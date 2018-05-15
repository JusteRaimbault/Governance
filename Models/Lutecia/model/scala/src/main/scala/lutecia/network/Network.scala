package lutecia.network

import lutecia.Lutecia

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case class Network(
                  nodes: Seq[Node],
                  links: Seq[Link],
                  distances: Seq[Seq[Double]], /** distance matrix within the network */
                  patchesDistances: Seq[Seq[Double]] /** distance matrix between all patches */
                  )


object Network {

  /**
    * Constructor for an initial network
    * @param n
    * @param l
    */
  def apply(n: Seq[Node],l: Seq[Link]): Network = Network(n,l,Seq.empty,Seq.empty)

  /**
    * Constructor adding a single link
    *   TODO: question here, should we still use the dynamical programming as in the NetLogo implementation - heavy and maybe not useful
    *     -> do first speed tests
    * @param previousNetwork
    * @param l
    * @return
    */
  def apply(previousNetwork: Network,l: Link): Network = {previousNetwork}


  def testShortestPaths(): Unit = {
    // example from tuto scala graph
    import scalax.collection.edge.WDiEdge
    import scalax.collection.edge.Implicits._
    import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._
    import scalax.collection.Graph

    //val g = Graph(1~2 % 4, 2~3 % 2, 1~>3 % 5, 1~5  % 3, 3~5 % 2, 3~4 % 1, 4~>4 % 1, 4~>5 % 0)
    //def n(outer: Int): g.NodeT = g.get(outer)
    //println(g.get(1).shortestPathTo(g.get(5)))

    import scalax.collection.generator._

    //val predefined = RandomGraph.tinyConnectedIntDi(Graph)
    object sparse_Int extends RandomGraph.IntFactory {
      //val rng = new Random
      val order = 100
      val nodeDegrees = NodeDegreeRange(1,8)
      //val weightFactory = () => rng.nextLong()
      override def connected = false
    }
    val randomSparse = RandomGraph[Int,UnDiEdge,Graph](
      Graph, sparse_Int, Set(UnDiEdge))
    val g = randomSparse.draw
    println(g)
    //g.nodes.map{case n => println(n.value)}
    //println(g.edges.map{_.weight})

    val rng = new Random

    // test all shortest paths
    var t = System.currentTimeMillis()
    val pathsdijkstra = mutable.Map[(Node,Node),Path]()
    for(o <- g.nodes;d <- g.nodes) {
      //def randomWeight(e:g.EdgeT) = rng.nextLong()
      //val path = o.shortestPathTo(d,randomWeight)
      val path = o.shortestPathTo(d)
      if (path != None) pathsdijkstra((Node(o.value),Node(d.value))) = Path(path.get.nodes.map{case n => Node(n.value)}.toList)
      //println(path)
    }
    val allpaths1 = pathsdijkstra.toMap

    val test: (Node,Node) = (Node(g.nodes.toSeq(0).value),Node(g.nodes.toSeq(90).value))
    println(allpaths1(test))
    println("Dijsktra : "+(System.currentTimeMillis()-t)/1000.0)

    t = System.currentTimeMillis()
    val nodeseq: Seq[Node] = g.nodes.map{case n=>Node(n.value)}.toSeq
    val linkseq: Seq[Link] = g.edges.map{case e=>Link(e._1.value,e._2.value,rng.nextDouble())}.toSeq
    val nw = Network(nodeseq,linkseq)
    val allpaths2 = GraphAlgorithm.allPairsShortestPath(nw)
    println(allpaths2(test))
    println("Floid : "+(System.currentTimeMillis()-t)/1000.0)

    println(allpaths1==allpaths2)

    //for(k <- allpaths1.keySet){if(allpaths1(k)!=allpaths2(k)){println(allpaths1(k));println(allpaths2(k));println()}}
    //proportion of different paths
    var diff = 0.0
    for(k <- allpaths1.keySet){if(allpaths1(k)!=allpaths2(k))diff+=1.0}
    println(diff/allpaths1.keySet.size)
  }


}









