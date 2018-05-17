

package lutecia.indicators

import lutecia.core.{Cell, Grid, World}


object Indicators {

  def computeResultIndicators(result: Result) = {

    println(totalDiffActives(result))

  }


  /**
    * compute state indicators
    * @param world
    */
  def computeStateIndicators(world: World) = {

    println("--------------------------")
    println("Step "+world.time)
    println("  Average accessibility = "+averageCellValue(_.accessibility)(world))
    println("  Average distance = "+NetworkIndicators.averageDistance(world))
    println("  Average actives = "+averageCellValue(_.actives)(world))
    println("  Average employments = "+averageCellValue(_.employments)(world))
    println("  Average aUtility = "+averageCellValue(_.aUtility)(world))
    println("  Average eUtility = "+averageCellValue(_.eUtility)(world))
  }

  /**
    *
    * @param worlds
    */
  def computeStatesIndicators(worlds: Seq[World]) = {
    println("  Actives diff = "+diffActives(worlds))
  }


  /**
    * more general indic for average values
    * @param fun
    * @param world
    * @return
    */
  def averageCellValue(fun:Cell=>Double)(world: World): Double = {
    val flatvals = world.cells.cells.flatten.map(fun)
    flatvals.sum/flatvals.size
  }

  /**
    *
    * @param world
    * @return
    */
  def averageAccessibility(world: World): Double = {
    val flataccess =world.cells.cells.flatten.map{_.accessibility}
    flataccess.sum/flataccess.size
  }

  def averageActives(world: World): Double = {
    val flatactives = world.cells.cells.flatten.map{_.actives}
    flatactives.sum/flatactives.size
  }

  def averageEmployments(world: World): Double = {
    val flatemployments = world.cells.cells.flatten.map(_.employments)
    flatemployments.sum/flatemployments.size
  }

  /**
    * active diffs between two consecutive time steps
    * @param states
    * @return
    */
  def diffActives(states: Seq[World]): Double = {
    Grid.absGridDiff(states(states.size-1).cells,states(states.size-2).cells,_.actives)
  }


  /**
    * total diff actives
    * @param result
    * @return
    */
  def totalDiffActives(result: Result): Double = {
    result.states.sliding(2,1).toSeq.map{case s=>Grid.absGridDiff(s(0).cells,s(1).cells,_.actives)}.sum
  }

}



object NetworkIndicators {

  /**
    * Average distance between each pair of node
    * @param world
    * @return
    */
  def averageDistance(world: World): Double = {
    val flatdists = world.network.distances.flatten
    flatdists.sum/flatdists.size
  }


}




