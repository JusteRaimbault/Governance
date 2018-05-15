
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
  def evolveLandUse(world: World,lutecia: Lutecia): World = {
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
  def computeUtilities(cells: Grid,lutecia: Lutecia): Grid = {
    val gammaA = lutecia.gammaA
    val gammaE = lutecia.gammaE
    val aFormFactors = cells.cells.flatten.map(aFormFactor).map{Math.pow(_,1-gammaA)}
    val eFormFactors = cells.cells.flatten.map(eFormFactor).map{Math.pow(_,1-gammaE)}

    val aUtilities = cells.cells.flatten.map{case c => Math.pow(c.atoeAccessibility,gammaA)}.zip(aFormFactors).map{case (x1,x2) => x1*x2}
    val eUtilities = cells.cells.flatten.map{case c => Math.pow(c.etoaAccessibility,gammaE)}.zip(eFormFactors).map{case (x1,x2) => x1*x2}

    Grid(flatCells = cells.cells.flatten.zip(aUtilities.zip(eUtilities)).map{case (c,(a,e)) => Cell(c,(a,e),"utilities")},lutecia.worldSize)
  }

  /**
    * Compute accessibilities
    * @param cells
    * @param network
    * @param lutecia
    * @return
    */
  def computeAccessibilities(cells: Grid,network: Network,lutecia: Lutecia): Grid = {
    val lambda = lutecia.lambda
    val aAccess = cells.cells.flatten.map{case c => c.actives*(network.patchesDistances(c.number).map{case d => Math.exp(-lambda*d)}.zip(cells.cells.flatten.map{_.employments}).map{case(x1,x2)=>x1*x2}.sum)}
    val eAccess = cells.cells.flatten.map{case c => c.employments*(network.patchesDistances(c.number).map{case d => Math.exp(-lambda*d)}.zip(cells.cells.flatten.map{_.actives}).map{case(x1,x2)=>x1*x2}.sum)}
    //val access = aAccess.zip(eAccess).map{case (a,e) => a+e}
    Grid(flatCells = cells.cells.flatten.zip(aAccess.zip(eAccess)).map{case (c,(aa,ae))=>Cell(c,(aa,ae),"accessibilities")},lutecia.worldSize)
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


