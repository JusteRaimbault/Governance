
package lutecia.core

import java.io.FileWriter

import lutecia.Lutecia
import lutecia.governance.Mayor
import lutecia.luti.Luti
import lutecia.network._

case class World(
                grid: Grid,
                network: Network,
                mayors: Seq[Mayor],
                time: Int
                ) {

  //override def toString: String

}


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
    val compCells = Luti.computeUtilities(Luti.computeAccessibilities(world.grid,world.network,lutecia),lutecia)
    World(compCells,world.network,world.mayors,t)
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


  /**
    *
    * @param world
    * @param pathPrefix
    */
  def exportWorld(world: World,
                  pathPrefix: String,
                  format: String = "csv"): Unit = {
    format match {
      case "csv" => {
        // dump params
        println("Exporting world with : Network |V| = "+world.network.nodes.size+" |E| = "+world.network.links.size+ " ; "+world.mayors.size+" mayors")

        // write cells
        val writer: FileWriter = new FileWriter(pathPrefix + "_grid.csv")
        writer.write("id;x;y;actives;employments\n")
        world.grid.cells.flatten.foreach {
          (c: Cell) => writer.write(Seq(c.number, c.x, c.y, c.actives, c.employments).mkString(";") + "\n")
        }
        writer.close()

        // write mayors
        val writerMayors: FileWriter = new FileWriter(pathPrefix + "_mayors.csv")
        writerMayors.write("position;x;y\n")
        world.mayors.foreach { (m: Mayor) => writerMayors.write(Seq(m.position.number,m.position.x,m.position.y).mkString(";")+ "\n") }
        writerMayors.close()
        // write network
        val writerNetworkNodes: FileWriter = new FileWriter(pathPrefix + "_networkNodes.csv")
        writerNetworkNodes.write("id;x;y\n")
        world.network.nodes.foreach { (n: Node) => writerNetworkNodes.write(Seq(n.id, n.x, n.y).mkString(";") + "\n") }
        writerNetworkNodes.close()
        val writerNetworkLinks: FileWriter = new FileWriter(pathPrefix + "_networkLinks.csv")
        writerNetworkLinks.write("id1;id2;speed")
        world.network.links.foreach { (l: Link) => writerNetworkLinks.write(Seq(l.e1.id,l.e2.id,l.speed).mkString(";") + "\n") }
        writerNetworkLinks.close()
      }

      case "" => ()
    }
  }



}
