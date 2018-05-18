
package lutecia.network

case class Link(
               e1: Node, /** origin node */
               e2: Node, /** destination node */
               length: Double, /** real length */
               speed: Double, /** max speed : used to compute effective distance*/
               cost: Double, /** generalized cost used to compute shortest paths*/
               capacity: Double, /** capacity (compared with flows to have effective speed) */
               flow: Double /** traffic flow in the link */
               )


object Link {

  /**
    * Euclidian distance between two nodes
    * @param e1
    * @param e2
    * @return
    */
  def distance(e1: Node,e2:Node): Double = {
    math.sqrt(math.pow(e1.x-e2.x,2) + math.pow(e1.y-e2.y,2))
  }

  /**
    * Link with default speed
    * @param e1
    * @param e2
    * @return
    */
  def apply(e1: Node,e2: Node): Link = Link(e1,e2,distance(e1,e2),1.0,1.0,1.0,1.0)

  /**
    * Link with precised speed but no flow/capacity
    * @param e1
    * @param e2
    * @param s
    * @return
    */
  def apply(e1: Node, e2: Node, s: Double): Link = {
    val d = distance(e1,e2)
    Link(e1,e2,d,s,d/s,1.0,1.0)
  }

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
