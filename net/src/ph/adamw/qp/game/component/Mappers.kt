package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

object Mappers {
    val map = HashMap<Class<Component>, ComponentMapper<Component>>()

    fun <T : Component> get(clazz: Class<T>) : ComponentMapper<T> {
        if(!map.containsKey(clazz)) {
            map[clazz as Class<Component>] = ComponentMapper.getFor(clazz)
        }

        return map[clazz]!! as ComponentMapper<T>
    }
}