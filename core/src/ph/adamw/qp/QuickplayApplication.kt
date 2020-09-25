package ph.adamw.qp

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuickplayApplication : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private var accumulator = 0f

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        ClientEndpoint.attemptConnect("0.0.0.0", 3336)
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stepPhysicsWorld()

        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
    }

    private fun stepPhysicsWorld() {
        val delta = Gdx.graphics.deltaTime

        accumulator += delta.coerceAtMost(0.25f)

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME
            //world.step(STEP_TIME, 6, 2)
        }
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }

    companion object {
        private const val STEP_TIME = 1f / 60f
        val localManager = GameManager(false)
    }
}