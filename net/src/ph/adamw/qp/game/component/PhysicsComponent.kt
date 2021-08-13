package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.game.component.util.BodyData
import ph.adamw.qp.game.component.util.UpdatableComponent
import java.io.Serializable
import kotlin.math.floor
import kotlin.math.round

class PhysicsComponent(private val bodyDef: BodyDef) : Component, UpdatableComponent, Serializable {
    @Transient
    lateinit var world: World

    @Transient
    lateinit var body : Body

    // Used for deserialization and serialization
    lateinit var bodyData : BodyData

    private val fixtures = ArrayList<FixtureDef>()

    fun createBody() {
        body = world.createBody(bodyDef)
        for(i in fixtures) {
            body.createFixture(i)
        }

        // Load body data state into active body
        if(::bodyData.isInitialized) {
            copyBodyDataFrom(bodyData)
        }
    }

    private fun copyBodyDataFrom(bd: BodyData) {
        // TODO fix the fact that if the world becomes locked during this execution we will crash
        // --> maybe sync it into the game thread
        body.setTransform(bd.position, bd.angle)
        body.linearVelocity = bd.linearVelocity
        body.angularVelocity = bd.angularVelocity
        body.angularDamping = bd.angularDamping
        body.massData = bd.massData
        body.isBullet = bd.bullet
        body.angularDamping = bd.angularDamping
        body.isSleepingAllowed = bd.sleepingAllowed
        body.gravityScale = bd.gravityScale
    }

    fun removeBody() {
        world.destroyBody(body)
    }

    fun isReady() : Boolean {
        return ::body.isInitialized
    }

    fun createFixture(fixture: FixtureDef) : PhysicsComponent {
        fixtures.add(fixture)
        return this
    }

    // Save body state into a body data object
    fun createBodyData() {
        if (!isReady()) {
            return
        }

        bodyData = BodyData(
            Vector2(round(body.position.x), round(body.position.y)),
            body.linearVelocity,
            body.angularVelocity,
            body.angle,
            body.massData,
            body.isBullet,
            body.angularDamping,
            body.isSleepingAllowed,
            body.gravityScale
        )
    }

    override fun update(new: Component) {
        val p = new as PhysicsComponent

        Gdx.app.postRunnable {
            if(p::bodyData.isInitialized && !world.isLocked) {
                copyBodyDataFrom(p.bodyData)
            }
        }
    }
}