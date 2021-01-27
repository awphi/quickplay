package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import java.io.Serializable

class IDComponent : Component, Serializable {
    var id : Long = -1L
}