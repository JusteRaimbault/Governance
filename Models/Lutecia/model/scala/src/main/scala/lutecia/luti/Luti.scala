
package lutecia.luti

import lutecia.Lutecia
import lutecia.core._
import lutecia.network.Network


object Luti {


  /**
    *
    * @param world
    * @return
    */
  def evolveLandUse(world: World)(implicit lutecia: Lutecia): World = {
    /**
      * - compute accessibilities
      * - compute form factors
      * - compute utilities
      *    -> all done at world creation
      * - relocate : done here
      */
    world
  }

  /**
    *
    * @param cells
    * @return
    */
  def computeUtilities(cells: Grid)(implicit lutecia: Lutecia): Grid = {
    val aFormFactors = cells.cells.flatten.map(aFormFactor).map{Math.pow(_,1-lutecia.gammaA)}
    val eFormFactors = cells.cells.flatten.map(eFormFactors).map{Math.pow(_,1-lutecia.gammaE)}

    val aUtilities = cells.cells.flatten.map{case c => Math.pow(c.atoeAccessibility,lutecia.gammaA)}.zip(aFormFactors).map{case (x1,x2) => x1*x2}
    val eUtilities = cells.cells.flatten.map{case c => Math.pow(c.etoaAccessibility,lutecia.gammaE)}.zip(eFormFactors).map{case (x1,x2) => x1*x2}

    Grid(flatCells = cells.cells.flatten.zip(aUtilities.zip(eUtilities)).map{case (c,(a,e)) => Cell(c,(a,e),"utilities")})
  }

  /**
    * Compute accessibilities
    * @param cells
    * @param network
    * @param lutecia
    * @return
    */
  def computeAccessibilities(cells: Grid,network: Network)(implicit lutecia: Lutecia): Grid = {
    val aAccess = cells.cells.flatten.map{case c => c.actives*(network.patchesDistances(c.number).map{case d => Math.exp(-lutecia.lambda*d)}.zip(cells.cells.flatten.map{_.employments}).map{case(x1,x2)=>x1*x2}.sum)}
    val eAccess = cells.cells.flatten.map{case c => c.employments*(network.patchesDistances(c.number).map{case d => Math.exp(-lutecia.lambda*d)}.zip(cells.cells.flatten.map{_.actives}).map{case(x1,x2)=>x1*x2}.sum)}
    //val access = aAccess.zip(eAccess).map{case (a,e) => a+e}
    Grid(flatCells = cells.cells.flatten.zip(aAccess.zip(eAccess)).map{case (c,(aa,ae))=>Cell(c,(aa,ae),"accessibilities")})
  }

  /**
    * Actives form factor
    * @param cell
    * @return
    */
  def aFormFactor(cell: Cell): Double = {1/(cell.actives*cell.employments)}

  /**
    * Employments form factor
    * @param cell
    * @return
    */
  def eFormFactor(cell: Cell): Double = 1.0


}


