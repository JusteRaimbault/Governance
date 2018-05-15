
package lutecia.core

import lutecia.Lutecia
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
  def apply(world: World,lutecia: Lutecia): World = {
    // compute accessibilities and utilities
    val compCells = Luti.computeUtilities(Luti.computeAccessibilities(world.cells,world.network,lutecia),lutecia)
    World(compCells,world.network)
  }

}
