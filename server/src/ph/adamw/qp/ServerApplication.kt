package ph.adamw.qp

import com.badlogic.ashley.core.Entity
import mu.KotlinLogging
import ph.adamw.qp.game.PongGame
import ph.adamw.qp.io.JsonUtils

object ServerApplication {
    // TODO eventually move to hosting multiple game servers at once (on different ports of course)
    val testServer = GameServer()

    @JvmStatic
    fun main(args: Array<String>) {
        testServer.manager.init(PongGame())
        testServer.listenForConnections(3336)
        testServer.run()

        val e = PongGame.ball(testServer.manager.getGame().world)
        val j = JsonUtils.toJsonTree(e)
        print(j)
        val e2 = JsonUtils.fromJson(j, Entity::class.java)
        println(e2)
    }
}