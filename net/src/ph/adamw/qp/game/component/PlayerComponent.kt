package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import java.io.Serializable

class PlayerComponent : Component, Serializable {
    var owner : Long = NULL_PID
    var isLocked : Boolean = false

    companion object {
        const val NULL_PID = -1L
    }
}