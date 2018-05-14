
package lutecia.core

import lutecia.Lutecia


case class Grid(
               cells: Seq[Seq[Cell]]
               )


object Grid {

    def apply(flatCells: Seq[Cell])(implicit lutecia: Lutecia): Grid = {
      Grid(flatCells.sliding(lutecia.worldSize,lutecia.worldSize).toSeq)
    }
}

