package lutecia.network

import lutecia.Lutecia

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scalax.collection.{Graph, GraphEdge}
import scalax.collection.edge.Implicits._
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal._
import scalax.collection.edge.WUnDiEdge


case class Network(
                  nodes: Seq[Node],
                  links: Seq[Link],
                  paths: Map[(Node,Node),Path],
                  distancesMap: Map[(Node,Node),Double], /** distance map from node k1 (== patch k1) to node k2*/
                  distances: Seq[Seq[Double]] /** distance matrix */
                  //distances: Seq[Seq[Double]] /** distance matrix within the network */
                  //patchesDistances: Seq[Seq[Double]] /** distance matrix between all patches */
                  )


object Network {

    val empty = Network(Seq.empty,Seq.empty,Map.empty,Map.empty,Seq.empty)

  /**
    * Constructor for an initial network
    * @param n
    * @param l
    */
  def apply(n: Seq[Node],l: Seq[Link],computeDist: Boolean): Network = {
    if(computeDist) {
      // compute shortest paths
      val network = Network(n, l, Map.empty, Map.empty, Seq.empty)
      val paths = GraphAlgorithm.allPairsShortestPath(network)
      //println(paths.keySet.size)
      //println(n.size*n.size)
      val distMap = paths.mapValues { case p => p.cost }
      val distMat = Seq.tabulate(n.size, n.size) { case (i, j) => distMap((n(i), n(j))) } // note : no issue here as the network is necessarily connected
      Network(n, l, paths, distMap, distMat)
    }else{
      Network(n,l,Map.empty,Map.empty,Seq.empty)
    }
  }

  /**
    * construct and compute distances
    * @param n
    * @param l
    * @return
    */
  def apply(n: Seq[Node], l:Seq[Link]): Network = Network(n,l,true)


  /**
    * construct from links only
    * @param links
    */
  def apply(links: Seq[Link],computeDist: Boolean): Network = {
    val nodes = links.map{case l =>Seq(l.e1.id,l.e2.id)}.flatten.toSet.toSeq.map{(i: Int) =>Node(i)}
    Network(nodes,links,computeDist)
  }


  /**
    * idem with existing nodes
    * @param links
    * @param nodeMap
    * @param computeDist
    * @return
    */
  def apply(links: Seq[Link],nodeMap: Map[Int,Node],computeDist: Boolean): Network = {
    val nodes = links.map{case l =>Seq(l.e1.id,l.e2.id)}.flatten.toSet.toSeq.map{nodeMap(_)}
    Network(nodes,links,computeDist)
  }


  /**
    * @param links
    * @return
    */
  def apply(links: Seq[Link]): Network = {
    Network(links,true)
  }


  /**
    * Constructor adding a single link
    *   TODO: question here, should we still use the dynamical programming as in the NetLogo implementation - heavy and maybe not useful
    *     -> do first speed tests
    *
    *     Note : DO NOT add nodes, new infrastructures should snap to existing, consistent with snappingThreshold
    *
    * @param previousNetwork
    * @param l
    * @return
    */
  def apply(previousNetwork: Network,l: Link): Network = {
    Network(previousNetwork.nodes,previousNetwork.links++Seq(l))
  }


  /**
    * Add a set of links to a network
    *   Note : the added links only are planarized ? NO as tunnel effect not taken into account in the original implementation.
    *   (could be an option ?)
    * @param previousNetwork
    * @param links
    * @return
    */
  def apply(previousNetwork: Network, links: Seq[Link]): Network = {
    // TODO : planarize links
    Network(previousNetwork.nodes,previousNetwork.links++links)
  }

  /**
    * convert a Network to a Graph object
    * @param network
    * @return
    */
  def networkToGraph(network: Network): (Graph[Int,WUnDiEdge],Map[Int,Node]) = {
    var linklist = ArrayBuffer[WUnDiEdge[Int]]()
    for(link <- network.links){linklist.append(link.e1.id~link.e2.id % link.cost)}
    (Graph.from(linklist.flatten,linklist.toList),network.nodes.map{(n:Node)=>(n.id,n)}.toMap)
  }

  /**
    *
    * @param graph
    * @return
    */
  def graphToNetwork(graph: Graph[Int,WUnDiEdge],nodeMap: Map[Int,Node]): Network = {
    val links = ArrayBuffer[Link]();val nodes = ArrayBuffer[Node]()
    for(edge <-graph.edges){
      //links.append(Link(edge._1,edge._2,edge.weight))
      nodes.append(nodeMap(edge._1),nodeMap(edge._2))
      links.append(Link(nodeMap(edge._1),nodeMap(edge._2),edge.weight))
    }
    Network(nodes.toSet.toSeq,links,false)
  }


  /**
    * extract connected components
    * @param network
    * @return
    */
  def connectedComponents(network: Network): Seq[Network] = {
    val (graph,nodeMap) = networkToGraph(network)
    val components: Seq[graph.Component] = graph.componentTraverser().toSeq
    //val components: Seq[Int] = for (component <- graph.componentTraverser()) yield 0 /: component.nodes
    components.map{case c => graphToNetwork(c.toGraph,nodeMap)}
  }

  /**
    * get largest connected component
    * @param network
    * @return
    */
  def largestConnectedComponent(network: Network): Network = {
    val components = connectedComponents(network)
    val largestComp = components.sortWith{case(n1,n2)=>n1.nodes.size>=n2.nodes.size}(0)
    largestComp
  }


  /**
    * simplify a network
    * @param network
    * @return
    */
  def simplifyNetwork(network: Network): Network = {
    network
  }




  /*
    * convert a path object to a distance matrix
    *  -- not needed --
    * @param paths
    * @return
    */
  /*def shortestPathsToDistanceMatrix(paths: Map[(Node,Node),Path]):Seq[Seq[Double]] = {
    Seq.fill(1,1)(0.0)
  }*/


  /**
    * test shortest paths algorithms
    */
  def testShortestPaths(): Unit = {
    // example from tuto scala graph
    import scalax.collection.edge.WDiEdge



    //val g = Graph(1~2 % 4, 2~3 % 2, 1~>3 % 5, 1~5  % 3, 3~5 % 2, 3~4 % 1, 4~>4 % 1, 4~>5 % 0)
    //def n(outer: Int): g.NodeT = g.get(outer)
    //println(g.get(1).shortestPathTo(g.get(5)))

    import scalax.collection.generator._

    //val predefined = RandomGraph.tinyConnectedIntDi(Graph)
    object sparse_Int extends RandomGraph.IntFactory {
      //val rng = new Random
      val order = 200
      val nodeDegrees = NodeDegreeRange(1,5)
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
      if (path != None) pathsdijkstra((Node(o.value),Node(d.value))) = Path(path.get.nodes.map{case n => Node(n.value)}.toList,Seq.empty)
      //println(path)
    }
    val allpaths1 = pathsdijkstra.toMap

    //val test: (Node,Node) = (Node(g.nodes.toSeq(0).value),Node(g.nodes.toSeq(9).value))
    //println(allpaths1(test))
    println(allpaths1)
    println("Dijsktra : "+(System.currentTimeMillis()-t)/1000.0)

    t = System.currentTimeMillis()
    val nodeseq: Seq[Node] = g.nodes.map{case n=>Node(n.value)}.toSeq
    val linkseq: Seq[Link] = g.edges.map{case e=>Link(e._1.value,e._2.value,rng.nextDouble())}.toSeq
    val nw = Network(nodeseq,linkseq)
    val allpaths2 = GraphAlgorithm.allPairsShortestPath(nw)
    //println(allpaths2(test))
    println(allpaths2)
    println("Floid : "+(System.currentTimeMillis()-t)/1000.0)

    println(allpaths1==allpaths2)

    //for(k <- allpaths1.keySet){if(allpaths1(k)!=allpaths2(k)){println(allpaths1(k));println(allpaths2(k));println()}}
    //proportion of different paths
    var diff = 0.0
    for(k <- allpaths1.keySet){if(allpaths1(k)!=allpaths2(k))diff+=1.0}
    println(diff/allpaths1.keySet.size)
  }


}









