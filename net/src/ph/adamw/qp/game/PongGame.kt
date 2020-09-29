package ph.adamw.qp.game

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.*
import ph.adamw.qp.game.component.ControlledComponent
import ph.adamw.qp.game.component.OwnedComponent
import ph.adamw.qp.game.component.DrawableComponent
import ph.adamw.qp.game.component.PhysicsComponent
import ph.adamw.qp.game.input.PongPaddleInputController


class PongGame : AbstractGame() {
    override val name: String
        get() = "Pong"
    override val minPlayers: Int
        get() = 2
    override val maxPlayers: Int
        get() = 2

    val controlMapper = ComponentMapper.getFor(OwnedComponent::class.java)

    private val leftPaddle = Paddle(world, true)
    private val rightPaddle = Paddle(world, false)
    private val ball = Ball(world)

    override fun init() {
        if(!manager.isHost) {
            leftPaddle.add(DrawableComponent(Gdx.files.internal("pong/paddle.png")))
            rightPaddle.add(DrawableComponent(Gdx.files.internal("pong/paddle.png")))
            ball.add(DrawableComponent(Gdx.files.internal("pong/ball.png")))
        }

        manager.engine.addEntity(leftPaddle)
        manager.engine.addEntity(rightPaddle)
        manager.engine.addEntity(ball)
    }

    private fun getPaddle(pid: Long) : Paddle? {
        if(controlMapper.get(leftPaddle).owner == pid) {
            return leftPaddle
        } else if(controlMapper.get(rightPaddle).owner == pid) {
            return rightPaddle
        }

        return null
    }

    override fun onConnect(pid: Long) {
        super.onConnect(pid)

        val p = getPaddle(OwnedComponent.NULL_PID) ?: return
        controlMapper.get(p).owner = pid

        if(!manager.isHost) {
            p.add(ControlledComponent(PongPaddleInputController))
        }
    }

    override fun onDisconnect(pid: Long) {
        super.onDisconnect(pid)
        controlMapper.get(getPaddle(pid)).owner = OwnedComponent.NULL_PID
    }

    class Paddle(world: World, left: Boolean) : Entity() {
        init {
            add(OwnedComponent())

            val bodyDef = BodyDef()
            bodyDef.type = BodyDef.BodyType.KinematicBody

            val x1 = ((GameConstants.WORLD_WIDTH / 2f) - 100f) / GameConstants.PPM
            val x = if(left) {
                -x1
            } else {
                x1
            }

            bodyDef.position.set(x, 0f)
            val comp = PhysicsComponent(bodyDef, world)
            add(comp)
            val fixture = FixtureDef()
            val rect = PolygonShape()
            rect.setAsBox(1f, 4f)
            fixture.shape = rect
            comp.body.createFixture(fixture)
            rect.dispose()
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
            val comp = PhysicsComponent(bodyDef, world)
            add(comp)
            comp.body.createFixture(fixture)
            circle.dispose()

            comp.body.applyForceToCenter(-800f, 0f, true)
        }
    }
}