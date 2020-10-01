package ph.adamw.qp.game.listener

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.game.component.Mappers

class EntityIDProvider : EntityListener {
    private val map = HashMap<Long, Entity>()
    private val mapper = Mappers.get(IDComponent::class.java)
    private var nextId = 0L

    private fun nextId() : Long {
        do {
            nextId ++
        } while(map.containsKey(nextId))

        return nextId
    }

    override fun entityAdded(entity: Entity) {
        var component = mapper.get(entity)

        if(component == null) {
            component = IDComponent()
            entity.add(component)
        }

        val id = nextId()
        component.id = id
        map[id] = entity
    }

    override fun entityRemoved(entity: Entity) {
        val component = mapper.get(entity)
        if(component != null) {
            map.remove(component.id)
        }
    }
}