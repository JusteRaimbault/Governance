
package lutecia.network

case class Link(
               e1: Node,
               e2: Node,
               capacity: Double,
               speed: Double,
               flow: Double
               )


object Link {

  def apply(e1: Node,e2: Node): Link = Link(e1,e2,1.0,1.0,1.0)

}
