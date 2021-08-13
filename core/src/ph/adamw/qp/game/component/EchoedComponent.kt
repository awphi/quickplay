package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.entity.EntityUtils

class EchoedComponent(val echo: Entity) : Component {
    companion object {
        fun addEcho(toEcho: Entity) {
            val cp = EntityUtils.copyEntity(toEcho)

            cp.remove(PlayerComponent::class.java)
            cp.remove(IDComponent::class.java)

            cp.add(EchoComponent(toEcho))

            QuickplayApplication.localManager.engine.addEntity(cp)
            toEcho.add(EchoedComponent(cp))
        }
    }
}