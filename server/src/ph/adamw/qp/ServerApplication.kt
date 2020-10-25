package ph.adamw.qp

import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.pong.PongGame

object ServerApplication {
    private val testServer = GameServer()

    @JvmStatic
    fun main(args: Array<String>) {
        testServer.manager.init(PongGame())
        testServer.listenForConnections(GameConstants.TCP_PORT)
        testServer.run()
    }
}