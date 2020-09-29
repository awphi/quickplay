package ph.adamw.qp.game.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.PhysicsComponent


class Box2DSystem(private val world: World) : IntervalSystem(GameConstants.TICK_STEP) {
    override fun updateInterval() {
        world.step(GameConstants.TICK_STEP, 6, 2)
    }
}