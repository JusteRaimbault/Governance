
package lutecia.governance

import lutecia.core.Cell


case class Mayor(
                position: Cell,
                governed: Set[Cell],
                wealth: Double
                )



object Mayor {

  def apply(c: Cell) = Mayor(c,Set.empty,0.0)

}

