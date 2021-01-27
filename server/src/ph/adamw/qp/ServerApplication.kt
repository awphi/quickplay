package ph.adamw.qp

import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.system.EntityUpdateSystem
import ph.adamw.qp.pong.PongGame

object ServerApplication {
    private val testServer = GameServer()

    @JvmStatic
    fun main(args: Array<String>) {
        testServer.manager.init(PongGame())
        testServer.manager.engine.addSystem(EntityUpdateSystem(testServer))
        testServer.listenForConnections(GameConstants.DEFAULT_PORT)
        testServer.run()
    }
}