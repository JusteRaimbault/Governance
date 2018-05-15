
package lutecia

import lutecia.core._
import lutecia.governance.Governance
import lutecia.luti.Luti
import lutecia.transportation.Transportation
import scala.util.Random

trait Lutecia {
  /**
    * random number generator
    */
  val rng = new Random

  val lutecia: Lutecia = this

  /**
    * size of the grid
    */
  def worldSize: Int = 20

  /**
    * number of territories
    */
  def numberTerritories: Int = 3


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
    Governance.evolveNetwork(Luti.evolveLandUse(Transportation.assignTransportation(world),lutecia))
  }


  def states = Iterator.iterate(initialWorld)(nextState)


}



object Lutecia {

}






