

package lutecia.indicators

import lutecia.core.{Grid, World}


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
    println("  Average accessibility = "+averageAccessibility(world))
    println("  Average distance = "+NetworkIndicators.averageDistance(world))

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




