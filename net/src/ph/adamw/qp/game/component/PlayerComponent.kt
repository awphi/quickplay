package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import ph.adamw.qp.game.component.util.UpdatableComponent
import ph.adamw.qp.game.input.InputHandler
import java.io.Serializable

class PlayerComponent(var inputHandler: InputHandler?) : Component, Serializable, UpdatableComponent {
    var owner : Long = NULL_PID
    var isLocked : Boolean = false

    companion object {
        const val NULL_PID = -1L
    }

    override fun update(new: Component) {
        val p = new as PlayerComponent
        owner = p.owner
        isLocked = p.isLocked
        inputHandler = p.inputHandler
    }
}