package ph.adamw.qp.game.input

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ph.adamw.qp.game.component.Mappers
import ph.adamw.qp.game.component.PhysicsComponent

object PongPaddleInputController : EntityInputController() {
    private const val SPEED = 20f

    override fun process(entity: Entity, deltaTime: Float) {
        var f = 0f

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            f += SPEED
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            f -= SPEED
        }

        Mappers.get(PhysicsComponent::class.java).get(entity).body.setLinearVelocity(0f, f)
    }
}