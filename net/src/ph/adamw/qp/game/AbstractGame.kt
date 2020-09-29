package ph.adamw.qp.game

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.GameManager

abstract class AbstractGame {
    @Transient
    val world = World(Vector2(0f, -10f), true)

    abstract val name : String
    abstract val minPlayers: Int
    abstract val maxPlayers: Int

    abstract fun init(manager: GameManager)

    open fun onConnect(pid: Long) {}
    open fun onDisconnect(pid: Long) {}
}