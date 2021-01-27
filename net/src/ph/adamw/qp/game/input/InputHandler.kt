package ph.adamw.qp.game.input

import com.badlogic.ashley.core.Entity

interface InputHandler {
    fun handle(input: InputSnapshot, entity: Entity)
}