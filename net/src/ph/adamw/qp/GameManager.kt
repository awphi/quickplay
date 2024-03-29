package ph.adamw.qp

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
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

open class GameManager {
    protected val entityIDProvider = EntityIDProvider()
    protected val logger = KotlinLogging.logger {}
    private lateinit var game: AbstractGame
    var time : Float = 0f
        protected set

    val engine = Engine()
    val world = World(Vector2(0f, -10f), true)

    private val inputSnapshots = HashMap<Long, InputSnapshot>()

    val packetRegistry : PacketRegistry by lazy {
        PacketRegistry(this)
    }

    init {
        logger.info("Started new game manager!")
        packetRegistry.build()
        engine.addEntityListener(entityIDProvider)
        engine.addEntityListener(EntityBodyProvider(world))
        engine.addSystem(InputHandlerSystem(this))
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

    fun getEntity(id: Long) : Entity? {
        return entityIDProvider.get(id)
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

    open fun onEntityInputHandled(input: InputSnapshot, entity: Entity) {

    }
}