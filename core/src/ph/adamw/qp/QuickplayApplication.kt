package ph.adamw.qp

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import mu.KotlinLogging
import ph.adamw.qp.game.listener.EntityDrawableProvider
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.input.InputSnapshot
import ph.adamw.qp.game.system.DrawSystem
import ph.adamw.qp.game.system.InputSampleSystem


class QuickplayApplication : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport : Viewport
    private lateinit var debugRenderer : Box2DDebugRenderer

    private val logger = KotlinLogging.logger {}

    override fun create() {
        val inputSnapshot = InputSnapshot()
        Gdx.input.inputProcessor = inputSnapshot

        batch = SpriteBatch()
        camera = OrthographicCamera()
        viewport = FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera)
        debugRenderer = Box2DDebugRenderer()

        localManager.engine.addEntityListener(EntityDrawableProvider())
        localManager.engine.addSystem(DrawSystem(batch))
        localManager.engine.addSystem(InputSampleSystem(inputSnapshot))

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        camera.update()

        //DEBUG
        ClientEndpoint.fakeLag = 50f
        ClientEndpoint.connect("localhost", GameConstants.DEFAULT_PORT)
        logger.info("Assigned local port: ${ClientEndpoint.tcpSocket.localPort}")

        /*
        val game = PongGame()
        val json = JsonUtils.toJsonTree(game)
        println(json)
        val rebuiltGame = JsonUtils.fromJson(json, AbstractGame::class.java)
        localManager.init(rebuiltGame)
         */
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
        val localManager = GameManager()
    }
}