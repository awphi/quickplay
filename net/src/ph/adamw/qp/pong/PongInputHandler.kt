package ph.adamw.qp.pong

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input
import ph.adamw.qp.game.component.util.Mappers
import ph.adamw.qp.game.component.PhysicsComponent
import ph.adamw.qp.game.input.InputHandler
import ph.adamw.qp.game.input.InputSnapshot

object PongInputHandler : InputHandler {
    private val physMapper = Mappers.get(PhysicsComponent::class.java)

    override fun handle(input: InputSnapshot, entity: Entity) {
        var v = 0f

        if(input.isKeyDown(Input.Keys.UP)) {
            v += 25f
        }

        if(input.isKeyDown(Input.Keys.DOWN)) {
            v -= 25f
        }

        val b = physMapper.get(entity)
        if(b.isReady()) {
            b.body.setLinearVelocity(0f, v)
        }

        //TODO DEBUG println(b.body.position)
    }
}