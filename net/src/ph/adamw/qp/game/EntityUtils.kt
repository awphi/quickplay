package ph.adamw.qp.game

import com.badlogic.ashley.core.Entity
import ph.adamw.qp.game.component.util.Mappers
import ph.adamw.qp.game.component.util.UpdatableComponent

object EntityUtils {
    fun copyEntityInto(from: Entity, to: Entity) {
        copyEntityInto(from, to, false)
    }


    /**
     * Copy entity [from] into entity [to], entity [from] should be discarded afterwards as it's components will now be
     * used in [to] - will cause strange behaviour.
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