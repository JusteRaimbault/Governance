
package lutecia.network

case class Link(
               e1: Node,
               e2: Node,
               weight: Double,
               capacity: Double,
               speed: Double,
               flow: Double
               )


object Link {

  def apply(e1: Node,e2: Node): Link = Link(e1,e2,1.0,1.0,1.0,1.0)

  def apply(e1: Node, e2: Node, w: Double): Link = Link(e1,e2,w,1.0,1.0,1.0)

  def apply(n1:Int,n2:Int): Link = Link(Node(n1),Node(n2))

  def apply(n1:Int,n2:Int, w: Double): Link = Link(Node(n1),Node(n2),w)

}
