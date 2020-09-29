package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component

class ControllableComponent : Component {
    var owner : Long = NULL_PID

    companion object {
        const val NULL_PID = -1L
    }
}