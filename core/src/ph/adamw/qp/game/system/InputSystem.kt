package ph.adamw.qp.game.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import ph.adamw.qp.GameManager
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.ControlledComponent
import ph.adamw.qp.game.component.Mappers

class InputSystem : IteratingSystem(Family.all(ControlledComponent::class.java).get(), 1) {
    private val mapper = Mappers.get(ControlledComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if(entity == null) {
            return
        }

        val c = mapper.get(entity)

        if(c.owner != GameConstants.LOCAL_PID) {
            return
        }

        if(!c.isLocked) {
            c.inputController.process(entity, deltaTime)
        }
    }
}