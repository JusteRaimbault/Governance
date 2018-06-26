package lutecia.setup

import lutecia.Lutecia
import lutecia.core.{Grid, World}
import lutecia.network.{Link, Network, Node, SlimeMould}

import scala.collection.mutable.ArrayBuffer


/**
  * Network setups
  */


trait SyntheticNetwork extends SyntheticSetup {

}



/**
  * Empty network
  */
trait EmptyNetwork extends SyntheticNetwork {

  override def initialNetwork(world: World): Network = EmptyNetwork.emptyNetwork()

}

object EmptyNetwork {
  /**
    * Empty network
    * @return
    */
  //def emptyNetwork(): Network = Network(Seq.empty,Seq.empty)
  def emptyNetwork(): Network = Network.empty

}


/**
  * Grid network
  */
trait GridNetwork extends SyntheticNetwork {

  override def initialNetwork(world: World): Network = GridNetwork.gridNetwork(world.grid,this)

}

object GridNetwork {
  /**
    *
    * @param lutecia : the grid network needs global parameters
    * @return
    */
  def gridNetwork(grid: Grid,lutecia: Lutecia): Network = {
    val euclidianPace = lutecia.euclidianPace

    // create nodes
    val nodes: Seq[Seq[Node]] = grid.cells.map{case r => r.map{case c => Node(c)}}
    // create edges
    val edges = ArrayBuffer[Link]()
    //dirty
    for (i <- 0 to nodes.size - 1 ; j <- 0 to nodes(0).size - 1) {
      if(i-1>0){
        if(j-1>0){edges.append(Link(nodes(i)(j),nodes(i-1)(j-1),1/euclidianPace))}
        edges.append(Link(nodes(i)(j),nodes(i-1)(j),1/euclidianPace))
        if(j+1<nodes(0).size){edges.append(Link(nodes(i)(j),nodes(i-1)(j+1),1/euclidianPace))}
      }
      if(j-1>0){
        edges.append(Link(nodes(i)(j),nodes(i)(j-1),1/euclidianPace))
      }
      if(j+1<nodes(0).size){
        edges.append(Link(nodes(i)(j),nodes(i)(j+1),1/euclidianPace))
      }
      if(i+1<nodes.size){
        if(j-1>0){edges.append(Link(nodes(i)(j),nodes(i+1)(j-1),1/euclidianPace))}
        edges.append(Link(nodes(i)(j),nodes(i+1)(j),1/euclidianPace))
        if(j+1<nodes(0).size){edges.append(Link(nodes(i)(j),nodes(i+1)(j+1),1/euclidianPace))}
      }
    }
    Network(nodes.flatten,edges)
  }
}


/**
  * full network between mayors
  */
trait FullNetwork extends SyntheticNetwork {

  override def initialNetwork(world: World): Network = FullNetwork.fullNetwork(world,this)

}

object FullNetwork {

  /**
    *
    * @param world
    * @param lutecia
    * @return
    */
  def fullNetwork(world:World,lutecia: Lutecia): Network = {
    var nw = world.network
    // can add the full network to an existing network, otherwise use a grid
    if(nw==Network.empty){nw = GridNetwork.gridNetwork(world.grid,lutecia)}

    val mayors = world.mayors
    if(mayors.size==1){return nw}

    val linksToAdd = ArrayBuffer[Link]()
    // get links connecting mayors
    for(i <- 0 to (mayors.size - 2)){
      for(j <- (i+1) to (mayors.size - 1)){
        val m1 = mayors(i);val m2 = mayors(j)
        linksToAdd.append(Link(m1.position.number,m2.position.number,1.0)) // rapid link speed is 1, since grid link speed is 1/ euclidianPace
      }
    }
    Network(nw,linksToAdd)
  }

}



/**
  * Slime mould network
  */
trait SlimeMouldNetwork extends SyntheticNetwork {

  /**
    * gamma
    * @return
    */
  def gammaSlimeMould: Double = 1.8


  /**
    * threshold to discard links
    */
  def thresholdSlimeMould: Double = 0.1

  /**
    * initial diameter
    * @return
    */
  def initialDiameterSlimeMould: Double = 1.0

  /**
    * input flow (I_0)
    * @return
    */
  def inputFlowSlimeMould: Double = 1.0

  /**
    * number of time steps to generate the network
    * @return
    */
  def timeStepsSlimeMould: Int = 500

  def deltatSlimeMould: Double = 0.05

  /**
    * construct a background grid network ?
    * @return
    */
  def withGridNetwork: Boolean = true

  // rq here : impose nw generation to depend on the whole world
  override def initialNetwork(world: World): Network = SlimeMould.generateSlimeMould(world,this,withGridNetwork)


}




