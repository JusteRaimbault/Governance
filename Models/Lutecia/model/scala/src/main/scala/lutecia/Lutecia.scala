
package lutecia

import lutecia.core._
import lutecia.governance.Governance
import lutecia.luti.Luti
import lutecia.transportation.Transportation

trait Lutecia {

  /**
    * size of the grid
    */
  def worldSize: Int


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


  /**
    * Current world of the model
    */
  def world: World

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
    Governance.evolveNetwork(Luti.evolveLandUse(Transportation.assignTransportation(world)))
  }


  def states = Iterator.iterate(initialWorld)(nextState)


}



object Lutecia {

}






