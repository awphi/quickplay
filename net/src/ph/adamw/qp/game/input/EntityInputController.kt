package ph.adamw.qp.game.input

import com.badlogic.ashley.core.Entity

abstract class EntityInputController {
    abstract fun process(entity: Entity, deltaTime: Float)
}