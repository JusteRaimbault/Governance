package lutecia.network

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


object GraphAlgorithm {

  /**
    *
    *  Taken from https://gist.github.com/vy/5056542
    *
    * Returns shortest paths between all pairs using Floyd-Warshall algorithm.
    * Nodes are assumed to be enumerated without any holes and enumeration
    * starts from 0.
    *
    * @param network
    * @return shortest paths between all pairs, including the source and destination
    *       set of paths is roughly Map[Int, Map[Int, Seq[Int]]] ~ Map[(Int,Int),Path]
    *       or more simply a set of Paths (include o/d) but Map is better -> can directly retrieve paths
    */
  def allPairsShortestPath(network: Network): Map[(Node,Node),Path] = {
    println("computing shortest paths for network of size : |V|="+network.nodes.size+" ; |E|="+network.links.size)
    // nodes: Set[Int], links: Map[Int, Set[Int]]
    val nodenames = network.nodes.map{_.id}
    val nodeids: Map[Int,Int] = nodenames.zipWithIndex.toMap
    //val revnodeids: Map[Int,Int] = nodenames.zipWithIndex.map{case(oid,ind)=>(ind,oid)}.toMap
    val revnodes: Map[Int,Node] = network.nodes.zipWithIndex.map{case(n,i)=>(i,n)}.toMap
    val nodes = nodeids.keySet //not necessary, for clarity
    val mlinks = mutable.Map[Int, Set[Int]]()
    val mlinkweights = mutable.Map[(Int,Int),Double]()
    val links = mutable.Map
    for(link <- network.links){
      if(!mlinks.keySet.contains(nodeids(link.e1.id))) mlinks(nodeids(link.e1.id))=Set.empty[Int]
      if(!mlinks.keySet.contains(nodeids(link.e2.id))) mlinks(nodeids(link.e2.id))=Set.empty[Int]
      // links assumed undirected in our case
      mlinks(nodeids(link.e1.id))+=nodeids(link.e2.id)
      mlinks(nodeids(link.e2.id))+=nodeids(link.e1.id)
      mlinkweights((nodeids(link.e1.id),nodeids(link.e2.id)))=link.weight
      mlinkweights((nodeids(link.e2.id),nodeids(link.e1.id)))=link.weight
    }

    val links = mlinks.toMap
    //println(mlinkweights.keySet.size)
    val linkweights = mlinkweights.toMap
    //println(linkweights.keySet==mlinkweights.keySet)
    //println(links)
    //println(linkweights.keySet)
    //println(mlinkweights.keySet)

    val n = nodes.size
    val inf = Int.MaxValue

    // Initialize distance matrix.
    val ds = Array.fill[Double](n, n)(inf)
    for (i <- 0 until n) ds(i)(i) = 0.0
    for (i <- links.keys) {
      for (j <- links(i)) {
        //ds(i)(j) = 1
        ds(i)(j) = linkweights((i,j))
      }
    }

    // Initialize next vertex matrix.
    val ns = Array.fill[Int](n, n)(-1)

    // Here goes the magic!
    for (k <- 0 until n; i <- 0 until n; j <- 0 until n)
      if (ds(i)(k) != inf && ds(k)(j) != inf && ds(i)(k) + ds(k)(j) < ds(i)(j)) {
        ds(i)(j) = ds(i)(k) + ds(k)(j)
        ns(i)(j) = k
      }

    // Helper function to carve out paths from the next vertex matrix.
    def extractPath(path: ArrayBuffer[Node], i: Int, j: Int) {
      if (ds(i)(j) == inf) return
      val k = ns(i)(j)
      if (k != -1) {
        extractPath(path, i, k)
        path.append(revnodes(k))
        extractPath(path, k, j)
      }
    }

    // Extract paths.
    //val pss = mutable.Map[Int, Map[Int, Seq[Int]]]()
    val paths = mutable.Map[(Node,Node),Path]()
    for (i <- 0 until n) {
      //val ps = mutable.Map[Int, Seq[Int]]()
      for (j <- 0 until n)
        if (ds(i)(j) != inf) {
          //val p = new ArrayBuffer[Int]()
          val currentPath = new ArrayBuffer[Node]()
          currentPath.append(revnodes(i))
          if (i != j) {
            extractPath(currentPath, i, j)
            currentPath.append(revnodes(j))
          }
          paths((revnodes(i),revnodes(j))) = Path(revnodes(i),revnodes(j),currentPath.toList)
        }
    }

    // Return extracted paths.
    paths.toMap
  }


}




