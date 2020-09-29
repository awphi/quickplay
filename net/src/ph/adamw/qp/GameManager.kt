package ph.adamw.qp

import com.badlogic.ashley.core.Engine
import mu.KotlinLogging
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.system.Box2DSystem
import ph.adamw.qp.packet.PacketRegistry

class GameManager(val isHost: Boolean) {
    private val logger = KotlinLogging.logger {}
    private lateinit var game: AbstractGame
    val engine = Engine()

    val packetRegistry : PacketRegistry by lazy {
        PacketRegistry(this)
    }

    init {
        logger.info("Started new game manager!")
        packetRegistry.build()
    }

    fun getGame() : AbstractGame {
        return this.game
    }

    fun init(game: AbstractGame) {
        engine.removeSystem(engine.getSystem(Box2DSystem::class.java))
        engine.addSystem(Box2DSystem(game.world))

        this.game = game
        this.game.manager = this
        this.game.init()
    }

    fun tick(delta: Float) {
        engine.update(delta)
    }
}