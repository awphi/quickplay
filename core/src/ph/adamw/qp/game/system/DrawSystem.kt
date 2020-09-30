package ph.adamw.qp.game.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.DrawableComponent
import ph.adamw.qp.game.component.Mappers
import ph.adamw.qp.game.component.PhysicsComponent


class DrawSystem(private val batch: SpriteBatch) : IteratingSystem(Family.all(DrawableComponent::class.java).get(), 100) {
    private val mapper = Mappers.get(DrawableComponent::class.java)
    private val physMapper = Mappers.get(PhysicsComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val sprite = mapper.get(entity).sprite
        val phys = physMapper.get(entity)

        if(phys != null) {
            sprite.setOriginBasedPosition(phys.body.position.x * GameConstants.PPM, phys.body.position.y * GameConstants.PPM)
        }

        sprite.draw(batch)
    }

    override fun update(deltaTime: Float) {
        batch.begin()
        super.update(deltaTime)
        batch.end()
    }
}