import lutecia.core.World
import lutecia.{Lutecia, RunModel}
import lutecia.indicators.Result
import lutecia.network.{Network, SlimeMould}
import lutecia.setup._




object RunTest extends App {

  val t = System.currentTimeMillis()

  val model = new Lutecia with SyntheticSetup with ExponentialMixtureGrid with GridNetwork {
    //override def worldSize = 30
    override def finalTime: Int = 10
    //define parameters
    override def lambda: Double = 0.05
    override def beta: Double = 1.8
    override def alpha: Double = 0.1
    override def gammaA: Double = 0.9
    override def gammaE: Double = 0.8
  }


  //Test.testNetwork()
  //Test.testGridNetwork(model)
  //Test.testNetworkExport(model)
  //Test.testSlimeMould()
  Test.testRun(model)

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


  def testNetworkExport(model: Lutecia) = {
    val world = model.initialWorld
    World.exportWorld(world,"/home/raimbault/ComplexSystems/Governance/Models/Lutecia/model/netlogo6/setup/test/gridnw")
  }



  def testGridNetwork(model: Lutecia) = {
    val world = model.initialWorld
  }


  def testNetwork() = {
    Network.testShortestPaths()
  }


}