package lutecia.setup

import lutecia._
import lutecia.core._
import lutecia.governance.Mayor

import scala.util.Random

trait ExternalGrid extends SyntheticSetup {

  def actives: RasterLayer

  def employments: RasterLayer

  override def initialGrid: (Grid, Seq[Mayor]) = {
    val coords = Seq.tabulate(actives.size,actives.size){case (i,j) => (i,j)}
    val flatCells = actives.flatten.toSeq.zip(employments.flatten.toSeq).zip(coords.flatten).zipWithIndex.map {
      case (((a, e), (i, j)), k) => {
        val c = Cell(k, i.toDouble, j.toDouble);
        Cell(c, (a, e), "values")
      }
    }
    (Grid(flatCells,actives.size),Seq.empty)
  }

}
