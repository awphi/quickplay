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

    private val fixtures = ArrayList<FixtureDef>()

    fun createBody() {
        body = world.createBody(bodyDef)
        for(i in fixtures) {
            body.createFixture(i)
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
}