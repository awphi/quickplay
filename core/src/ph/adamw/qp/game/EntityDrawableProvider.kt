package ph.adamw.qp.game

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.Gdx
import ph.adamw.qp.game.component.DrawableComponent

class EntityDrawableProvider : EntityListener {
    override fun entityAdded(entity: Entity) {
        entity.add(DrawableComponent(Gdx.files.internal("pong/" + entity::class.java.simpleName.toLowerCase() + ".png")))
    }

    override fun entityRemoved(entity: Entity?) {}
}