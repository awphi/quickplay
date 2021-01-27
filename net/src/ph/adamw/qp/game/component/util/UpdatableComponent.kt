package ph.adamw.qp.game.component.util

import com.badlogic.ashley.core.Component

interface UpdatableComponent {
    fun update(new: Component)
}