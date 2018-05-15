
package lutecia.network

//
case class Path(
               o: Node,
               d: Node,
               path: Seq[Node],
               cost: Double
               )


object Path {

  def apply(o:Node,d:Node,path:Seq[Node]): Path = Path(o,d,path,0.0)

  def apply(path:Seq[Node]): Path = Path(path(0),path(path.size-1),path,0.0)

}



