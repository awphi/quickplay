package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class DrawableComponent(var sprite: Sprite) : Component {
    constructor(texture: Texture) : this(Sprite(texture))
    constructor(fileHandle: FileHandle) : this(Texture(fileHandle))

    init {
        sprite.setOriginCenter()
    }
}