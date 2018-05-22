

package lutecia.network


import lutecia.core.World
import lutecia.setup.{SlimeMouldNetwork, GridNetwork}
import org.apache.commons.math3.linear.{SingularMatrixException, _}

import scala.util.Random


object SlimeMould {

  /**
    *
    * @param world : this world is assumed to have an empty network if slime mould is used for network generation
    *   (test it !)
    * @param slimeMould
    * @return
    */
  def generateSlimeMould(world: World,slimeMould: SlimeMouldNetwork): Network = {
    var nw = world.network
    if(nw==Network.empty){nw = GridNetwork.gridNetwork(world.grid,slimeMould)} //
    val pMat = networkToPaceMatrix(nw)
    var diameters = initialDiameterMatrix(nw,slimeMould.initialDiameterSlimeMould)

    // iterate
    for(t <- 0 to (slimeMould.timeStepsSlimeMould - 1)){
      diameters = iterationSlimeMould(world,pMat,diameters,slimeMould)
    }

    // extract the strong links
    val strongLinks: Seq[Link] = diameters.map{_.zipWithIndex}.zipWithIndex.map{case(row,i)=> row.map{case(d,j)=> (d,(i,j))}}.flatten.filter{case(d,_)=>d>slimeMould.thresholdSlimeMould}.map{case(_,(i,j))=>Link(Node(i),Node(j))}.toSeq

    // keep the largest connected components and add them to network
    //Network.largestConnectedComponent(Network(nw,strongLinks))
    val nwLargestComponent = Network.largestConnectedComponent(Network(strongLinks,false))
    Network(nw,nwLargestComponent.links)
  }



  /**
    * An iteration of the slime mould : given pace matrix and current diameters, computes next diameters.
    * @param world
    * @param P
    * @param D
    * @return
    */
  def iterationSlimeMould(world: World,P:Array[Array[Double]],D:Array[Array[Double]],slimeMould: SlimeMouldNetwork): Array[Array[Double]] = {
    // get od
    val (o,d)=chooseOD(world)
    // io flows
    val ioflows = getIOFlows(o,d,D,slimeMould.inputFlowSlimeMould)
    // flows
    val flows = getFlowMatrix(D,P)
    // solve system
    D
  }





  /**
    * convert an initial network to its adjacency matrix
    * assumes that node ids are in {0;|V|} ; network is not directed
    * @param network
    * @return
    */
  def networkToPaceMatrix(network: Network): Array[Array[Double]] = generalizedAdjacencyMatrix(1/_.length)(network)

  def initialDiameterMatrix(network: Network,initialDiameter: Double): Array[Array[Double]] = generalizedAdjacencyMatrix(_=>initialDiameter)(network)

  def generalizedAdjacencyMatrix(fun:Link=>Double)(network: Network): Array[Array[Double]] = {
    val n = network.nodes.size
    val M = Array.fill[Double](n,n)(0.0)
    for(link <- network.links){
      val (i,j) = (link.e1.id,link.e2.id)
      M(i)(j)=fun(link);M(j)(i)=fun(link)
    }
    M
  }

  /**
    * Get the matrix to be inverted, from diameter and pace matrix
    * @param D
    * @param P
    * @return
    */
  def getFlowMatrix(D: Array[Array[Double]],P: Array[Array[Double]]): Array[Array[Double]] = {
    val prod = D.flatten.toSeq.zip(P.flatten.toSeq).map{case(d,p)=> - d*p}.toArray.sliding(D.size,D.size).toArray
    for(i <- 0 to D.size - 1){prod(i)(i)= prod(i).sum}
    prod
  }

  /**
    * Get vector of io flows
    * @param origin index of origin node
    * @param destinations index of destination nodes
    * @param D
    * @return
    */
  def getIOFlows(origin: Int,destinations: Seq[Int],D: Array[Array[Double]],originFlow: Double): Array[Double] = {
    val res = Array.fill[Double](D.size)(0.0)
    res(origin)= originFlow
    for(d <- destinations){res(d) = - originFlow / destinations.size}
    res
  }


  /**
    * Choose o/d given the world and the network
    * @param world
    * @return
    */
  def chooseOD(world: World): (Int,Seq[Int]) = {
     val omayor = Random.shuffle(world.mayors).take(1).toSeq(0)
     val o = omayor.position.number
     val dests = world.mayors.toSet.-(omayor).map{_.position.number}.toSeq
    (o,dests)
  }





  /**
    * Solve a matrix system
    * @param M
    * @param B
    * @return
    */
  def solveSystem(M: Array[Array[Double]],B: Array[Double]): Array[Double] = {
    // convert the objects to math3 objects
    val matrix: RealMatrix = MatrixUtils.createRealMatrix(M)
    //ensure the matrix is invertible by perturbating it if necessary
    val inv = MatrixUtils.inverse(matrix)
    //try{}catch SingularMatrixException {}
    val res: RealVector = inv.operate(MatrixUtils.createRealVector(B))
    res.toArray
  }







}


