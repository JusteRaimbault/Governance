
package lutecia.core

import lutecia.Lutecia


case class Grid(
               cells: Seq[Seq[Cell]]
               )


object Grid {

    def apply(flatCells: Seq[Cell],worldSize: Int): Grid = {
      Grid(flatCells.sliding(worldSize,worldSize).toSeq)
    }
}

