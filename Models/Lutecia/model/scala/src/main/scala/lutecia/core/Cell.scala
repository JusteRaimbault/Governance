
package lutecia.core


case class Cell(
                 number: Int,
                 actives:Double,
                 employments: Double,
                 atoeAccessibility: Double,
                 etoaAccessibility: Double,
                 accessibility: Double,
                 aUtility: Double,
                 eUtility: Double
               )


object Cell {

  //def apply(n: Int,a: Double,e: Double) = Cell(n,a,e,0.0,0.0,0.0,0.0,0.0)
  //def apply(cell: Cell) = cell

  // update actives and employments
  //def apply(cell: Cell,actives: Double,employments: Double): Cell = cell.copy(actives=actives,employments=employments)
  // add accessibilities
  //def apply(cell: Cell,a:Double,e: Double,atot: Double): Cell = cell.copy(atoeAccessibility = a,etoaAccessibility = e,accessibility = atot)
  // add utilities
  //def apply(cell: Cell,autil: Double,eutil: Double): Cell = cell.copy(aUtility = autil,eUtility = eutil)

  // single constructor with purpose
  def apply(cell: Cell,values: (Double,Double),purpose: String): Cell = {
    purpose match {
      case "values" => cell.copy(actives=values._1,employments=values._2)
      case "utilities" => cell.copy(aUtility = values._1,eUtility = values._2)
      case "accessibilities" => cell.copy(atoeAccessibility = values._1,etoaAccessibility = values._2,accessibility = values._1 + values._2)
    }
  }

}
