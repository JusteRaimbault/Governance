
package lutecia.core

import lutecia.Lutecia
import lutecia.luti.Luti
import lutecia.network._

case class World(
                cells: Grid,
                network: Network,
                time: Int
                )


object World {


  /**
    * Warning : those constructors are quite dangerous
    */


  /**
    * Compute accessibilities and utilities at construction
    * @param world
    * @return
    */
  def apply(world: World,lutecia: Lutecia,t: Int): World = {
    // compute accessibilities and utilities
    val compCells = Luti.computeUtilities(Luti.computeAccessibilities(world.cells,world.network,lutecia),lutecia)
    World(compCells,world.network,t)
  }

  /**
    * Compute access but keep time
    * @param world
    * @param lutecia
    * @return
    */
  def apply(world: World,lutecia: Lutecia): World = World(world,lutecia,world.time)

  /**
    * no computation
    * @param world
    * @param t
    * @return
    */
  def apply(world: World,t: Int): World = world.copy(time=t)


}
