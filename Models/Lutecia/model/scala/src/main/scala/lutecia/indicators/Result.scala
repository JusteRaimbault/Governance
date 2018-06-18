
package lutecia.indicators

import lutecia.Lutecia
import lutecia.core.World


case class Result(
                 states: Seq[World],
                 model: Lutecia
                 ) {

  //override def toString: String = "Result : "+states.size+" states"
  override def toString: String = "Result(states = "+states.toString()+")"

}



object Result {




}



