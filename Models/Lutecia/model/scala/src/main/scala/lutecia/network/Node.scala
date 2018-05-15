
package lutecia.network

case class Node(
                 /**
                   * position
                   */
                 x: Double,
                 y: Double,
                 /**
                   * id
                   */
                 id: Int
               )


object Node {

  def apply(n:Int): Node = Node(0.0,0.0,n)

}