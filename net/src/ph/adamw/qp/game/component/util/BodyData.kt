package ph.adamw.qp.game.component.util

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.MassData

class BodyData(val position: Vector2,
               val linearVelocity : Vector2,
               val angularVelocity: Float,
               val angle: Float,
               val massData: MassData,
               val bullet: Boolean,
               val angularDamping: Float,
               val sleepingAllowed : Boolean,
               val gravityScale : Float)