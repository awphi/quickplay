package ph.adamw.qp

import com.badlogic.gdx.Gdx
import mu.KotlinLogging
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.packet.PacketRegistry

open class GameManager(val isHost: Boolean) {
    private val logger = KotlinLogging.logger {}
    lateinit var game: AbstractGame

    val packetRegistry : PacketRegistry by lazy {
        PacketRegistry(this)
    }

    init {
        logger.info("Started new game manager!")
        packetRegistry.build()
    }
}