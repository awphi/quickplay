package ph.adamw.qp.pong

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.*
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.*
import ph.adamw.qp.game.component.util.Mappers


class PongGame : AbstractGame() {
    override val name: String
        get() = "Pong"
    override val minPlayers: Int
        get() = 2
    override val maxPlayers: Int
        get() = 2

    @Transient
    private val controlMapper = Mappers.get(PlayerComponent::class.java)

    private val leftPaddle = createPaddle(true)
    private val rightPaddle = createPaddle(false)
    private val ball = createBall()

    override fun init() {
        manager.engine.addEntity(leftPaddle)
        manager.engine.addEntity(rightPaddle)
        manager.engine.addEntity(ball)
    }

    private fun getPaddle(pid: Long) : Entity? {
        if(controlMapper.get(leftPaddle).owner == pid) {
            return leftPaddle
        } else if(controlMapper.get(rightPaddle).owner == pid) {
            return rightPaddle
        }

        return null
    }

    override fun onConnect(pid: Long) {
        super.onConnect(pid)

        val p = getPaddle(PlayerComponent.NULL_PID) ?: return
        controlMapper.get(p).owner = pid
    }

    override fun onDisconnect(pid: Long) {
        super.onDisconnect(pid)
        controlMapper.get(getPaddle(pid)).owner = PlayerComponent.NULL_PID
    }

    companion object {
        fun createBall() : Entity {
            val e = Entity()
            e.add(NameComponent("pong", "ball"))
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
            e.add(PhysicsComponent(bodyDef).createFixture(fixture))
            return e
        }

        // TODO add slight curve to edge of paddle hit box
        fun createPaddle(left: Boolean) : Entity {
            val e = Entity()
            e.add(NameComponent("pong", "paddle"))
            e.add(PlayerComponent(PongInputHandler))
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
            e.add(PhysicsComponent(bodyDef).createFixture(fixture))
            return e
        }
    }
}