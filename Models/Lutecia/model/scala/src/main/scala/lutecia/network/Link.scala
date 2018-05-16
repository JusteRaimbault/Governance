
package lutecia.network

case class Link(
               e1: Node,
               e2: Node,
               length: Double,
               weight: Double,
               capacity: Double,
               speed: Double,
               flow: Double
               )


object Link {

  def distance(e1: Node,e2:Node): Double = {
    math.sqrt(math.pow(e1.x-e2.x,2) + math.pow(e1.y-e2.y,2))
  }

  def apply(e1: Node,e2: Node): Link = Link(e1,e2,distance(e1,e2),1.0,1.0,1.0,1.0)

  def apply(e1: Node, e2: Node, w: Double): Link = Link(e1,e2,distance(e1,e2),w,1.0,1.0,1.0)

  /**
    * note : with node numbers only link is not localized
    * @param n1
    * @param n2
    * @return
    */
  def apply(n1:Int,n2:Int): Link = Link(Node(n1),Node(n2))

  /**
    * idem
    * @param n1
    * @param n2
    * @param w
    * @return
    */
  def apply(n1:Int,n2:Int, w: Double): Link = Link(Node(n1),Node(n2),w)

}
