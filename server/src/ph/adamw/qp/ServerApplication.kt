package ph.adamw.qp

import ph.adamw.qp.game.PongGame

object ServerApplication {
    // Eventually: move to hosting multiple game servers at once (on different ports of course)
    private val testServer = GameServer()

    @JvmStatic
    fun main(args: Array<String>) {
        testServer.manager.init(PongGame())
        testServer.listenForConnections(3336)
        testServer.run()
    }
}