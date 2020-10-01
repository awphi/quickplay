package ph.adamw.qp.game.component

import com.badlogic.ashley.core.Component
import java.io.Serializable

class NameComponent(val domain: String, val name: String) : Component, Serializable