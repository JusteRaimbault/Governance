
package lutecia.network

import lutecia.core.Cell

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

  def apply(c: Cell): Node = Node(c.x,c.y,c.number)

}