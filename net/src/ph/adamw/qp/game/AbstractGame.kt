package ph.adamw.qp.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.GameManager

abstract class AbstractGame {
    @Transient
    lateinit var manager: GameManager

    abstract val name : String
    abstract val minPlayers: Int
    abstract val maxPlayers: Int

    abstract fun init()

    /**
     * Called before the GAME_UPDATE packet is sent to the client.
     */
    open fun onConnect(pid: Long) {}

    open fun onDisconnect(pid: Long) {}
}