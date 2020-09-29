package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World

class PhysicsComponent(bodyDef: BodyDef, world: World) : Component {
    val body = world.createBody(bodyDef)
}