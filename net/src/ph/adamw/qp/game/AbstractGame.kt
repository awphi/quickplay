package ph.adamw.qp.game

import com.badlogic.gdx.Input
import ph.adamw.qp.GameManager

abstract class AbstractGame {
    @Transient
    lateinit var manager: GameManager

    abstract val name : String
    abstract val minPlayers: Int
    abstract val maxPlayers: Int

    abstract fun init()

    /**
     * Called before the GAME_SET packet is sent to the client.
     */
    open fun onConnect(pid: Long) {}

    open fun onDisconnect(pid: Long) {}
}