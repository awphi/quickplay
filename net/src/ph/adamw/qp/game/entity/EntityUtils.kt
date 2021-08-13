package ph.adamw.qp.game.entity

import com.badlogic.ashley.core.Entity
import mu.KotlinLogging
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.game.component.util.Mappers
import ph.adamw.qp.game.component.util.UpdatableComponent
import ph.adamw.qp.io.JsonUtils

object EntityUtils {
    private val logger = KotlinLogging.logger {}
    private val idMapper = Mappers.get(IDComponent::class.java)

    fun copyEntity(from: Entity) : Entity {
        val entity = Entity()
        val cp = JsonUtils.fromJson(JsonUtils.toJsonTree(from), Entity::class.java)
        copyEntityInto(cp, entity, true)
        return entity
    }

    /**
     * Copy entity [from] into entity [to]
     * entity [from] should be discarded afterwards as it's components will now be in [to]
     */
    fun copyEntityInto(from: Entity, to: Entity, append: Boolean) {
        for(i in from.components) {
            val c = Mappers.get(i::class.java).get(to)

            if(c == null) {
                if(append) {
                    to.add(i)
                }

                continue
            } else if(c is UpdatableComponent) {
                // TODO work out how to do this
                c.update(i)
            }
        }
    }
}