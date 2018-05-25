
package lutecia.core

import lutecia.Lutecia


case class Grid(
               cells: Seq[Seq[Cell]]
               )


object Grid {

  /**
    * Constructor from flatcells
    * @param flatCells
    * @param worldSize
    * @return
    */
    def apply(flatCells: Seq[Cell],worldSize: Int): Grid = {
      Grid(flatCells.sliding(worldSize,worldSize).toSeq)
    }


  /**
    * update actives and employments (better not be as a constructor)
    *   NOTE : maybe should use monocle for such operations..
    * @param flatCells
    * @param flatActives
    * @param flatEmployments
    * @param worldSize
    * @return
    */
    def updateValues(flatCells: Seq[Cell],flatActives: Seq[Double],flatEmployments: Seq[Double],worldSize: Int): Grid = {
      Grid(flatActives.zip(flatEmployments).zip(flatCells).map{case ((a,e),c)=>Cell(c,(a,e),"values")},worldSize)
    }


  /**
    *
    * @param grid1
    * @param grid2
    * @param getvar
    * @return
    */
    def absGridDiff(grid1:Grid,grid2:Grid,getvar:Cell=>Double): Double = {
      grid1.cells.flatten.map(getvar).zip(grid2.cells.flatten.map(getvar)).map{case(x1,x2)=>math.abs(x1-x2)}.sum
    }

}

