package ph.adamw.qp.game.listener

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import ph.adamw.qp.game.component.DrawableComponent
import ph.adamw.qp.game.component.Mappers
import ph.adamw.qp.game.component.NameComponent

class EntityDrawableProvider : EntityListener {
    private val mapper = Mappers.get(NameComponent::class.java)

    override fun entityAdded(entity: Entity) {
        val c = mapper.get(entity) ?: return
        Gdx.app.postRunnable {
            entity.add(DrawableComponent(Gdx.files.internal("${c.domain}/entity/${c.name}.png")))
        }
    }

    override fun entityRemoved(entity: Entity?) {}
}