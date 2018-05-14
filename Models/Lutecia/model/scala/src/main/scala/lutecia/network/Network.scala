package lutecia.network



case class Network(
                  nodes: Seq[Node],
                  links: Seq[Link],
                  distances: Seq[Seq[Double]], /** distance matrix within the network */
                  patchesDistances: Seq[Seq[Double]] /** distance matrix between all patches */
                  )


object Network {

  /**
    * Constructor for an initial network
    * @param n
    * @param l
    */
  def apply(n: Seq[Node],l: Seq[Link]): Network = Network(n,l,Seq.empty,Seq.empty)

  /**
    * Constructor adding a single link
    *   TODO: question here, should we still use the dynamical programming as in the NetLogo implementation - heavy and maybe not useful
    *     -> do first speed tests
    * @param previousNetwork
    * @param l
    * @return
    */
  def apply(previousNetwork: Network,l: Link): Network = {previousNetwork}

}
