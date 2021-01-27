package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import ph.adamw.qp.game.component.util.UpdatableComponent
import java.io.Serializable

class NameComponent(var domain: String, var name: String) : Component, Serializable, UpdatableComponent {
    override fun update(new: Component) {
        val p = new as NameComponent
        domain = p.domain
        name = p.name
    }
}