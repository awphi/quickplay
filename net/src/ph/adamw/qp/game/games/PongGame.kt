package ph.adamw.qp.game.games

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.*
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.*
import ph.adamw.qp.game.input.PongPaddleInputController


class PongGame : AbstractGame() {
    override val name: String
        get() = "Pong"
    override val minPlayers: Int
        get() = 2
    override val maxPlayers: Int
        get() = 2

    private val controlledMapper = Mappers.get(ControlledComponent::class.java)

    // TODO entity (de/)serialization, see notes
    private val leftPaddle = Paddle(world, true)
    private val rightPaddle = Paddle(world, false)
    private val ball = Ball(world)

    override fun init() {
        manager.engine.addEntity(leftPaddle)
        manager.engine.addEntity(rightPaddle)
        manager.engine.addEntity(ball)
    }

    private fun getPaddle(pid: Long) : Paddle? {
        if(controlledMapper.get(leftPaddle).owner == pid) {
            return leftPaddle
        } else if(controlledMapper.get(rightPaddle).owner == pid) {
            return rightPaddle
        }

        return null
    }

    override fun onConnect(pid: Long) {
        super.onConnect(pid)

        val p = getPaddle(ControlledComponent.NULL_PID) ?: return
        controlledMapper.get(p).owner = pid
    }

    override fun onDisconnect(pid: Long) {
        super.onDisconnect(pid)
        controlledMapper.get(getPaddle(pid)).owner = ControlledComponent.NULL_PID
    }

    // TODO add slight curve to edge of paddle hit box
    class Paddle(world: World, left: Boolean) : Entity() {
        init {
            add(ControlledComponent(PongPaddleInputController))
            val bodyDef = BodyDef()
            bodyDef.type = BodyDef.BodyType.KinematicBody

            val x1 = ((GameConstants.WORLD_WIDTH / 2f) - 100f) / GameConstants.PPM
            val x = if(left) {
                -x1
            } else {
                x1
            }

            bodyDef.position.set(x, 0f)
            val fixture = FixtureDef()
            val rect = PolygonShape()
            rect.setAsBox(1f, 4f)
            fixture.shape = rect
            rect.dispose()

            add(PhysicsComponent(bodyDef, world).createFixture(fixture))
        }
    }

    class Ball(world: World) : Entity() {
        init {
            val bodyDef = BodyDef()
            bodyDef.type = BodyDef.BodyType.DynamicBody
            bodyDef.position.set(0f, 0f)
            bodyDef.gravityScale = 0f
            val fixture = FixtureDef()
            val circle = CircleShape()
            circle.radius = 1f
            fixture.shape = circle
            fixture.friction = 0f
            fixture.density = 0.15f
            fixture.restitution = 1f
            circle.dispose()

            add(PhysicsComponent(bodyDef, world).createFixture(fixture))
        }
    }
}