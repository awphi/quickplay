package ph.adamw.qp.game.listener

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.game.component.Mappers
import ph.adamw.qp.game.component.PhysicsComponent

class EntityBodyProvider(private val world: World) : EntityListener {
    private val mapper = Mappers.get(PhysicsComponent::class.java)

    override fun entityAdded(entity: Entity) {
        val c = mapper.get(entity) ?: return
        c.world = world
        c.createBody()
    }

    override fun entityRemoved(entity: Entity) {
        val c = mapper.get(entity) ?: return
        c.removeBody()
    }
}