
package lutecia.indicators

import lutecia.{Lutecia, RasterLayer}
import lutecia.core.{Cell, World}


case class Result(
                   /**
                     * Successive states of the model
                     */
                 states: Seq[World],

                   /**
                     * Model corresponding to this result
                     */
                 model: Lutecia
                 ) {

  //override def toString: String = "Result : "+states.size+" states"
  override def toString: String = "Result(states = "+states.toString()+",model = "+model.toString+")"

}



object Result {


  def getActivesTrajectories(result: Result) = getTrajectories(result,_.actives)
  def getEmploymentsTrajectories(result: Result) = getTrajectories(result,_.employments)

  /**
    * Get cells trajectories given a projection from a Cell
    * @param result
    * @param proj
    * @return
    */
  def getTrajectories(result: Result,proj: Cell=>Double): Seq[RasterLayer] = {
    result.states.map{_.grid.cells.toArray.map{case row => row.toArray.map{proj(_)}}}
  }


  def getDistanceMatrices(result: Result): Seq[Array[Array[Double]]] = result.states.map{_.network.distances}


}



