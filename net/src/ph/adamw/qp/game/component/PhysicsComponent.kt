package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import java.io.Serializable

// TODO work out why bodies are not being properly recreated on the client after being deserialized
//      Problem is: FixtureDef's shapes do not store vertex information in them
//      make a wrapper object around it (FixtureDefDelegate) etc. that stores a fixture def
//      as well as it's shape/vert info with a method to produce a full FixtureDef
//      then change shape exclusion strategy to exclude serializing shapes in FixtureDefs
//      and change the shapeRta to use the new delegate classes

// TODO write type adapter to use the velocity, location, rotation etc. of the active body
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