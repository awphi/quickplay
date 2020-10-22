package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import ph.adamw.qp.game.input.InputHandler
import java.io.Serializable

class PlayerComponent(val inputHandler: InputHandler?) : Component, Serializable {
    var owner : Long = NULL_PID
    var isLocked : Boolean = false

    companion object {
        const val NULL_PID = -1L
    }
}