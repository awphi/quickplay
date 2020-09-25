package ph.adamw.qp

import ph.adamw.qp.game.PongGame

object ServerApplication {
    // TODO eventually move to hosting multiple game servers at once (on different ports of course)
    val testServer = GameServer(true)

    @JvmStatic
    fun main(args: Array<String>) {
        testServer.game = PongGame()
        testServer.listenForConnections(3336)
    }
}