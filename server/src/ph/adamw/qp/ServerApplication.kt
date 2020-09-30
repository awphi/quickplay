package ph.adamw.qp

import ph.adamw.qp.game.games.PongGame
import ph.adamw.qp.util.JsonUtils

object ServerApplication {
    // TODO eventually move to hosting multiple game servers at once (on different ports of course)
    val testServer = GameServer()

    @JvmStatic
    fun main(args: Array<String>) {
        testServer.manager.init(PongGame())
        testServer.listenForConnections(3336)
        testServer.run()
        val j = JsonUtils.toJson(testServer.manager.getGame())
        println(j)
    }
}