
package lutecia

import lutecia.core._

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



}



object Lutecia {

  def nextState(world: World): World = {

  }

}






