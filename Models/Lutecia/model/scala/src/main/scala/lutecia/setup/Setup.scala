package lutecia.setup

import lutecia.Lutecia
import lutecia.core.World

trait Setup extends Lutecia {
  override def initialWorld: World
}
