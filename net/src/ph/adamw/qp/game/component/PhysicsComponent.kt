package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import java.io.Serializable

class PhysicsComponent(private val bodyDef: BodyDef) : Component, Serializable {
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
            body.setTransform(bodyData.position, bodyData.angle)
            body.linearVelocity = bodyData.linearVelocity
            body.angularVelocity = bodyData.angularVelocity
            body.angularDamping = bodyData.angularDamping
            body.massData = bodyData.massData
            body.isBullet = bodyData.bullet
            body.angularDamping = bodyData.angularDamping
            body.isSleepingAllowed = bodyData.sleepingAllowed
            body.isAwake = bodyData.awake
            body.gravityScale = bodyData.gravityScale
        }
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
        if(!isReady()) {
            return
        }

        bodyData = BodyData(body.position,
            body.linearVelocity,
            body.angularVelocity,
            body.angle,
            body.massData,
            body.isBullet,
            body.angularDamping,
            body.isSleepingAllowed,
            body.isAwake,
            body.gravityScale
        )
    }
}