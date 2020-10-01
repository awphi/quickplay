package ph.adamw.qp

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ph.adamw.qp.game.listener.EntityDrawableProvider
import ph.adamw.qp.game.GameConstants
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
        camera.update()

        localManager.engine.addEntityListener(EntityDrawableProvider())
        localManager.engine.addSystem(DrawSystem(batch))

        //DEBUG
        ClientEndpoint.attemptConnect("0.0.0.0", 3336)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.projectionMatrix = camera.combined
        localManager.tick(Gdx.graphics.deltaTime)

        if(localManager.isGameReady()) {
            debugRenderer.render(localManager.world, camera.combined.scl(GameConstants.PPM))
        }
    }

    override fun dispose() {
        debugRenderer.dispose()
        batch.dispose()
        ClientEndpoint.disconnectAndAlertServer()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    companion object {
        val localManager = GameManager(false)
    }
}