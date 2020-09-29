package ph.adamw.qp.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.GameManager
import ph.adamw.qp.util.InitOnceProperty

abstract class AbstractGame {
    @Transient
    val world = World(Vector2(0f, -10f), true)

    @Transient
    lateinit var manager: GameManager

    abstract val name : String
    abstract val minPlayers: Int
    abstract val maxPlayers: Int

    abstract fun init()

    open fun onConnect(pid: Long) {}
    open fun onDisconnect(pid: Long) {}
}