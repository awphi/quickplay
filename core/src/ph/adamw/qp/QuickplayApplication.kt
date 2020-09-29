package ph.adamw.qp

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.PongGame
import ph.adamw.qp.game.system.DrawSystem


class QuickplayApplication : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport : Viewport
    private lateinit var debugRenderer : Box2DDebugRenderer

    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        viewport = FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera)
        debugRenderer = Box2DDebugRenderer()

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        println(camera.position)
        camera.update()

        localManager.engine.addSystem(DrawSystem(batch))

        //DEBUG
        //ClientEndpoint.attemptConnect("0.0.0.0", 3336)
        localManager.init(PongGame())
        localManager.getGame().onConnect(LOCAL_PID)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.projectionMatrix = camera.combined
        localManager.tick(Gdx.graphics.deltaTime)
        debugRenderer.render(localManager.getGame().world, camera.combined.scl(GameConstants.PPM))
    }

    override fun dispose() {
        debugRenderer.dispose()
        batch.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    companion object {
        const val LOCAL_PID = 1L
        val localManager = GameManager(false)
    }
}