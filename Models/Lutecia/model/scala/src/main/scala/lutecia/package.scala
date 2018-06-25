

package object lutecia {


  /**
    * Raster types
    */

  type RasterLayer = Array[Array[Double]]

  type Raster = Seq[RasterLayer]


  /**
    * Vector types -> Generic from oml plugin ? or external library loaded by both ?
    */

  type Point = (Double,Double)

  type Graph = (Set[Int],Set[(Int,Int)])

  type SpatialGraph = (Set[(Int,Point)],Seq[(Int,Int)])



}
