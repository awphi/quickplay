package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import ph.adamw.qp.GameManager
import ph.adamw.qp.game.input.EntityInputController

class ControlComponent(val inputController: EntityInputController) : Component {
    var owner : Long = NULL_PID

    var isLocked : Boolean = false

    companion object {
        const val NULL_PID = -1L
    }
}