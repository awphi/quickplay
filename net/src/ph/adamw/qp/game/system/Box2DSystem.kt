package ph.adamw.qp.game.system

import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.physics.box2d.World
import ph.adamw.qp.game.GameConstants


class Box2DSystem(private val world: World) : IntervalSystem(GameConstants.TICK_STEP) {
    override fun updateInterval() {
        world.step(GameConstants.TICK_STEP, 6, 2)
    }
}