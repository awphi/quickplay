package ph.adamw.qp.game.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.component.ControlledComponent
import ph.adamw.qp.game.component.Mappers
import ph.adamw.qp.game.component.OwnedComponent

class InputSystem : IteratingSystem(Family.all(OwnedComponent::class.java, ControlledComponent::class.java).get(), 1) {
    private val mapper = Mappers.get(OwnedComponent::class.java)
    private val controlledMapper = Mappers.get(ControlledComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        println(entity?.javaClass?.simpleName)
        if(entity == null || mapper.get(entity).owner != QuickplayApplication.LOCAL_PID) {
            return
        }

        controlledMapper.get(entity).inputController.process(entity, deltaTime)
    }
}