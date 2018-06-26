import lutecia.core.World
import lutecia.{Lutecia, RunModel}
import lutecia.indicators.Result
import lutecia.network.{Network, SlimeMould}
import lutecia.setup._




object RunTest extends App {

  val t = System.currentTimeMillis()

  val model = new Lutecia with SyntheticSetup
    with ExponentialMixtureGrid
    //with ExternalGrid
    with GridNetwork {
        override def worldSize = 15
        override def finalTime: Int = 10
        override def numberTerritories: Int = 3
        //define parameters
        override def lambda: Double = 0.05
        override def beta: Double = 1.8
        override def alpha: Double = 0.1
        override def gammaA: Double = 0.9
        override def gammaE: Double = 0.8
      override def euclidianPace: Double = 5.0

      // test for external grid with a random setup here
    //override def actives = Array.tabulate(worldSize,worldSize){(i,j) => rng.nextDouble()}
    //override def employments= Array.tabulate(worldSize,worldSize){(i,j) => rng.nextDouble()}

  }


  //Test.testNetwork()
  //Test.testGridNetwork(model)
  //Test.testNetworkExport(model)
  //Test.testSlimeMould()
  //Test.testRun(model)
  Test.testNetworkDistances()

  println("Ellapsed Time : "+(System.currentTimeMillis()-t)/1000.0)
}



object Test {


  def testRun(model: Lutecia) = {
    val result = RunModel.run(model)
    println(result)
  }



  def testSlimeMould() = {
    val model = new Lutecia with SyntheticSetup with ExponentialMixtureGrid with SlimeMouldNetwork {
      override def gammaSlimeMould: Double = 1.2
      override def thresholdSlimeMould: Double = 0.25
      override def withGridNetwork: Boolean = false
    }
    //val world = model.initialWorld
    val (grid,mayors) = model.initialGrid
    val tempWorld = World(grid,EmptyNetwork.emptyNetwork(),mayors,0)
    val nw = SlimeMould.generateSlimeMould(tempWorld,model,false)
    World.exportWorld(World(grid,nw,mayors,0),"/home/raimbault/ComplexSystems/Governance/Models/Lutecia/model/netlogo6/setup/test/slimemould")
  }


  /**
    * export a network
    * @param model
    */
  def testNetworkExport(model: Lutecia) = {
    val world = model.initialWorld
    World.exportWorld(world,"/home/raimbault/ComplexSystems/Governance/Models/Lutecia/model/netlogo6/setup/test/gridnw")
  }


  def testNetworkDistances() = {
    val res1 = RunModel.run(new Lutecia with SyntheticSetup with ExponentialMixtureGrid with GridNetwork)
    val res2 = RunModel.run(new Lutecia with SyntheticSetup with ExponentialMixtureGrid with GridNetwork)
    val map1 = res1.states.map{_.network.distancesMap.map{case ((n1,n2),d) => ((n1.id,n2.id),d)}}
    val map2 = res2.states.map{_.network.distancesMap.map{case ((n1,n2),d) => ((n1.id,n2.id),d)}}
    println(map1.zip(map2).map{case (m1,m2) => m1.map{case ((i,j),d) => math.abs(d - m2((i,j)))}.sum})
  }



  /**
    * test the construction of the background grid network
    * @param model
    */
  def testGridNetwork(model: Lutecia) = {
    val world = model.initialWorld
    println(world.network.distances.map(_.max).max)
    println(world.network.distances.map(_.min).min)
    println(world.network.distances.map(_.sum).sum / (math.pow(world.grid.cells.size,4.0)))
    println(world.network.distances.size)
    println(world.network.links.map(_.cost).toSet)

    val uniqueDistances = world.network.distances.flatten.toSet.toSeq.sorted
    println(uniqueDistances)
  }


  def testNetwork() = {
    Network.testShortestPaths()
  }


}