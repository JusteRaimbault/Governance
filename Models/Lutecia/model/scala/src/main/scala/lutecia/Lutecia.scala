
package lutecia

import lutecia.core._
import lutecia.governance.Governance
import lutecia.indicators.{Indicators, Result}
import lutecia.luti.Luti
import lutecia.transportation.Transportation

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

trait Lutecia {
  /**
    * random number generator
    */
  val rng = new Random

  //val lutecia: Lutecia = new Lutecia(this) with Transportation
  /**
    * Meta-parameters
    */


  /**
    * size of the grid
    */
  def worldSize: Int = 15 //20

  /**
    * number of territories
    */
  def numberTerritories: Int = 3

  /**
    * Final time
    */
  def finalTime: Int = 10//200

  /**
    *
    */
  def euclidianPace: Double = 5.0


  /**
    * Model parameters
    */

  /**
    * Cost of energy
    */
  def lambda: Double = 0.05

  /**
    * Discrete choices relocation
    */
  def beta: Double = 1.8

  /**
    * Relocation rate
    */
  def alpha: Double = 0.1

  /**
    * Cobb douglas actives
    */
  def gammaA: Double = 0.9

  /**
    * Cobb douglas employments
    */
  def gammaE: Double = 0.8





  //Current world of the model -> not needed, immutable
  //def world: World

  /**
    * Initial state
    */
  def initialWorld: World



  /**
    * main loop
    * @param world
    * @return
    */
  def nextState(lutecia: Lutecia, world: World): World = {
    val trworld = lutecia match {
      case l: Lutecia with Transportation => Transportation.assignTransportation(l,world)
      case _ => world
    }
    val luworld = Luti.evolveLandUse(lutecia,trworld)
    val govworld = lutecia match {
      case l: Lutecia with Governance =>  Governance.evolveNetwork(luworld)
      case _ => luworld
    }
    timeStep(govworld)
  }

  /**
    * tick
    * @param world
    * @return
    */
  def timeStep(world: World): World = {
    val nextWorld = World(world,world.time+1)
    Indicators.printStateIndicators(nextWorld)
    nextWorld
  }

  //def states = Iterator.iterate(initialWorld)(nextState)


  override def toString: String = "Lutecia model with worldSize = "+worldSize+
    " ; numberTerritories = "+numberTerritories+" ; finalTime = "+finalTime

}



object Lutecia {

}





object RunModel {


  /**
    * Run the model
    * @param model
    * @return
    */
  def run(model: Lutecia): Result = {

    // run the simulation in time
    val states = ArrayBuffer[World]()
    states.append(model.initialWorld)
    Indicators.printStateIndicators(states(0))
    for(t <- 0 to model.finalTime - 1){
      //println(t)
      states.append(model.nextState(model,states.last))
      Indicators.printStatesIndicators(states)
    }

    Result(states,model)
  }
}




