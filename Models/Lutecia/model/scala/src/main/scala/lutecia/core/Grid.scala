
package lutecia.core

import lutecia.Lutecia


case class Grid(
               cells: Seq[Seq[Cell]]
               ){

  override def toString: String = "Grid(w="+cells.size+",h="+cells(0).size+")"
}


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
    * Get vector of cell values, given a projection, ordered by cell number
    * @param grid
    * @param getvar
    * @return
    */
    def getValues(grid: Grid,getvar: Cell=>Double = _.actives): Array[Double] = {
      val res = Array.fill[Double](grid.cells.map{_.size}.sum)(0.0)
      grid.cells.foreach(_.foreach{case c => res(c.number) = getvar(c)})
      res
    }



  /**
    * Absolute difference between two grids on a given projection
    * @param grid1
    * @param grid2
    * @param getvar
    * @return
    */
    def absGridDiff(grid1:Grid,grid2:Grid,getvar:Cell=>Double): Double = {
      grid1.cells.flatten.map(getvar).zip(grid2.cells.flatten.map(getvar)).map{case(x1,x2)=>math.abs(x1-x2)}.sum
    }

}

