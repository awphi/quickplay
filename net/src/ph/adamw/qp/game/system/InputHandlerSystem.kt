package ph.adamw.qp.game.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ph.adamw.qp.GameManager
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.Mappers
import ph.adamw.qp.game.component.PlayerComponent

class InputHandlerSystem(val manager: GameManager) : IntervalIteratingSystem(Family.all(PlayerComponent::class.java).get(), GameConstants.TICK_STEP, -1) {
    private val mapper = Mappers.get(PlayerComponent::class.java)

    override fun processEntity(entity: Entity) {
        val c = mapper.get(entity)
        val input = manager.getInput(c.owner)

        if(input != null) {
            c.inputHandler?.handle(input, entity)
        }
    }
}