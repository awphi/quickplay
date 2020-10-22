package ph.adamw.qp

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import mu.KotlinLogging
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.input.InputSnapshot
import ph.adamw.qp.game.listener.EntityBodyProvider
import ph.adamw.qp.game.listener.EntityIDProvider
import ph.adamw.qp.game.system.Box2DSystem
import ph.adamw.qp.game.system.InputHandlerSystem
import ph.adamw.qp.packet.PacketRegistry

class GameManager {
    private val logger = KotlinLogging.logger {}
    private lateinit var game: AbstractGame
    var time : Float = 0f
        private set

    val engine = Engine()
    val world = World(Vector2(0f, -10f), true)

    private val inputSnapshots = HashMap<Long, InputSnapshot>()

    val packetRegistry : PacketRegistry by lazy {
        PacketRegistry(this)
    }

    init {
        logger.info("Started new game manager!")
        packetRegistry.build()
        engine.addEntityListener(EntityIDProvider())
        engine.addEntityListener(EntityBodyProvider(world))
        engine.addSystem(InputHandlerSystem(this))
        // TODO input HANDLING system that iterates over all player components & uses game's input map
    }

    fun setInput(pid: Long, input: InputSnapshot) {
        inputSnapshots[pid] = input
    }

    fun getInput(pid: Long) : InputSnapshot? {
        return inputSnapshots[pid]
    }

    fun getGame() : AbstractGame {
        return this.game
    }

    fun isGameReady() : Boolean {
        return ::game.isInitialized
    }

    fun init(game: AbstractGame) {
        // Clear the physics world by remaking the system (cheapest way to do this)
        engine.removeSystem(engine.getSystem(Box2DSystem::class.java))
        engine.addSystem(Box2DSystem(world))

        this.game = game
        this.game.manager = this
        this.game.init()
    }

    fun tick(delta: Float) {
        engine.update(delta)
        time += delta
    }
}