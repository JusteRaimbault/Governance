
package lutecia.indicators

import lutecia.{Lutecia, RasterLayer}
import lutecia.core.{Cell, World}


case class Result(
                 states: Seq[World],
                 model: Lutecia
                 ) {

  //override def toString: String = "Result : "+states.size+" states"
  override def toString: String = "Result(states = "+states.toString()+")"

}



object Result {

  def getActivesTrajectories(result: Result) = getTrajectories(result,_.actives).toArray
  def getEmploymentsTrajectories(result: Result) = getTrajectories(result,_.employments).toArray

  def getTrajectories(result: Result,proj: Cell=>Double): Seq[RasterLayer] = {
    result.states.map{_.grid.cells.toArray.map{case row => row.toArray.map{proj(_)}}}
  }


}



