
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

  val lutecia: Lutecia = this

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
  def finalTime: Int = 200

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
  def lambda: Double

  /**
    * Discrete choices relocation
    */
  def beta: Double

  /**
    * Relocation rate
    */
  def alpha: Double

  /**
    * Cobb douglas actives
    */
  def gammaA: Double

  /**
    * Cobb douglas employments
    */
  def gammaE: Double





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
  def nextState(world: World): World = {
    timeStep(Governance.evolveNetwork(Luti.evolveLandUse(Transportation.assignTransportation(world),lutecia)))
  }

  /**
    * tick
    * @param world
    * @return
    */
  def timeStep(world: World): World = {
    val nextWorld = World(world,world.time+1)
    Indicators.computeStateIndicators(nextWorld)
    nextWorld
  }

  //def states = Iterator.iterate(initialWorld)(nextState)


}



object Lutecia {

}





object RunModel {

  def run(model: Lutecia): Result = {

    // run the simulation in time
    val states = ArrayBuffer[World]()
    states.append(model.initialWorld)
    Indicators.computeStateIndicators(states(0))
    for(t <- 0 to model.finalTime - 1){
      //println(t)
      states.append(model.nextState(states.last))
      Indicators.computeStatesIndicators(states)
    }

    Result(states,model)
  }
}




