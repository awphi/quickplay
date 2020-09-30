package ph.adamw.qp.game.entity

import com.badlogic.ashley.core.Entity
import com.google.gson.JsonElement
import ph.adamw.qp.game.component.Mappers

class EntityData {
    private val map = HashMap<Class<out DataComponent>, JsonElement>()

    fun get(clazz: Class<DataComponent>) : JsonElement? {
        return map[clazz]
    }

    fun put(component: DataComponent) {
        map[component::class.java] = component.produce()
    }

    fun isEmpty() : Boolean {
        return map.isEmpty()
    }

    fun inject(entity: Entity) {
        for(i in map.keys) {
            val c = Mappers.get(i).get(entity) ?: continue
            c.consume(map[i]!!)
        }
    }

    companion object {
        fun build(entity: Entity) : EntityData {
            val sync = EntityData()

            for(i in entity.components) {
                if(i !is DataComponent) {
                    continue
                }

                sync.put(i)
            }

            return sync
        }
    }
}