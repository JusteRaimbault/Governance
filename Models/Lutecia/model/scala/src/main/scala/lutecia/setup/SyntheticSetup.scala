
package lutecia.setup

import lutecia.core.{Cell, Grid, World}
import lutecia.governance.Mayor
import lutecia.network.{Link, Network, Node, SlimeMould}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Synthetic setup
  */
trait SyntheticSetup extends Setup {

  /**
    * Initial grid takes no arguments
    * @return the Grid and the set of mayors
    */
  def initialGrid: (Grid,Seq[Mayor])

  /**
    * The network will generally depends on the world (since also includes mayors)
    * @param world temporary world without network
    * @return
    */
  def initialNetwork(world: World): Network

  override def initialWorld: World = {
    val (grid,mayors) = initialGrid
    val tempWorld = World(grid,EmptyNetwork.emptyNetwork(),mayors,0)
    World(World(grid,initialNetwork(tempWorld),mayors,0),this)
  }

}



object SyntheticSetup {

}





