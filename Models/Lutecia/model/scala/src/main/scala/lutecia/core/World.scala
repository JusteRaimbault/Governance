
package lutecia.core

import lutecia.luti.Luti
import lutecia.network._

case class World(
                cells: Grid,
                network: Network
                )


object World {

  /**
    * Compute accessibilities and utilities at construction
    * @param world
    * @return
    */
  def apply(world: World): World = {
    // compute accessibilities and utilities
    val compCells = Luti.computeUtilities(Luti.computeAccessibilities(world.cells,world.network))
    World(compCells,world.network)
  }

}
