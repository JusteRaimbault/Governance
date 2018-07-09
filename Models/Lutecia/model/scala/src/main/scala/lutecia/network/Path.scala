
package lutecia.network

//
case class Path(
               o: Node,
               d: Node,
               pathNodes: Seq[Node],
               pathLinks: Seq[Link],
               cost: Double
               ){

  def updateCost(linkFlows: Map[(Int,Int),Link]): Path = {
    val newLinks = pathLinks.map{case l => Link(l.e1,l.e2,l.length,l.speed,linkFlows((l.e1.id,l.e2.id)).cost,l.capacity,linkFlows((l.e1.id,l.e2.id)).flow)}
    Path(
      o,
      d,
      pathNodes,
      newLinks,
      newLinks.map{_.cost}.sum
    )
  }

}


object Path {

  /**
    *
    * @param o
    * @param d
    * @param nodes
    * @param links
    * @return
    */
  def apply(o:Node,d:Node,nodes:Seq[Node],links: Seq[Link]): Path = Path(o,d,nodes,links,links.map{case l => l.length/l.speed}.sum)

  /**
    *
    * @param nodes
    * @param links
    * @return
    */
  def apply(nodes:Seq[Node],links:Seq[Link]): Path = Path(nodes(0),nodes(nodes.size-1),nodes,links)

}



