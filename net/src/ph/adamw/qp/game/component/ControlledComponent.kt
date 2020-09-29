package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ph.adamw.qp.game.input.EntityInputController

class ControlledComponent(val inputController: EntityInputController) : Component