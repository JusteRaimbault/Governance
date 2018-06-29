
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
  def evolveLandUse(lutecia: Lutecia,world: World): World = {
    /**
      * - compute accessibilities
      * - compute form factors
      * - compute utilities
      *    -> all done at world creation
      * - relocate : done here - and recompute access
      */
    World(relocations(world,lutecia),lutecia)
  }

  /**
    * Relocations
    * @param world
    * @param lutecia
    * @return
    */
  def relocations(world: World,lutecia: Lutecia): World = {
    val cells = world.grid.cells

    // relocation actives
    val actives = cells.flatten.map{_.actives}
    val aitot = actives.sum
    val maxau = cells.flatten.map{_.aUtility}.max
    val euia = cells.flatten.map{case c => math.exp(lutecia.beta*c.aUtility/maxau)}
    val euiatot = euia.sum
    val newactives = euia.zip(actives).map{case (ua,a) => ((1 - lutecia.alpha)*a) + ua*lutecia.alpha*aitot/euiatot}

    // relocation employments
    val employments = cells.flatten.map{_.employments}
    val eitot = employments.sum
    val maxeu = cells.flatten.map{_.eUtility}.max
    val euie = cells.flatten.map{case c => math.exp(lutecia.beta*c.eUtility/maxeu)}
    val euietot = euie.sum
    val newemployments = euie.zip(employments).map{case (ue,e) => ((1 - lutecia.alpha)*e) + ue*lutecia.alpha*eitot/euietot}
    World(Grid.updateValues(cells.flatten,newactives,newemployments,lutecia.worldSize),world.network,world.mayors,world.time)
  }


  /**
    *
    * @param cells
    * @return
    */
  def computeUtilities(cells: Grid,lutecia: Lutecia): Grid = {
    //println("Computing utilities...")
    val gammaA = lutecia.gammaA
    val gammaE = lutecia.gammaE
    val aFormFactors = cells.cells.flatten.map(aFormFactor).map{math.pow(_,1-gammaA)}
    val eFormFactors = cells.cells.flatten.map(eFormFactor).map{math.pow(_,1-gammaE)}

    val aUtilities = cells.cells.flatten.map{case c => math.pow(c.atoeAccessibility,gammaA)}.zip(aFormFactors).map{case (x1,x2) => x1*x2}
    val eUtilities = cells.cells.flatten.map{case c => math.pow(c.etoaAccessibility,gammaE)}.zip(eFormFactors).map{case (x1,x2) => x1*x2}

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
    //println("Computing accessibilities...")
    val lambda = lutecia.lambda
    val aAccess = cells.cells.flatten.map{case c => c.actives*(network.distances(c.number).map{case d => math.exp(-lambda*d)}.zip(cells.cells.flatten.map{_.employments}).map{case(x1,x2)=>x1*x2}.sum)}
    val eAccess = cells.cells.flatten.map{case c => c.employments*(network.distances(c.number).map{case d => math.exp(-lambda*d)}.zip(cells.cells.flatten.map{_.actives}).map{case(x1,x2)=>x1*x2}.sum)}
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


