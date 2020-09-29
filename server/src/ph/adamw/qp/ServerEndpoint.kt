package ph.adamw.qp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ph.adamw.qp.util.GameConstants
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class ServerEndpoint(val id: Long, val server: GameServer, val socket: Socket) : Endpoint(server.manager) {
    private val logger = KotlinLogging.logger {}

    override val outputStream: OutputStream = socket.getOutputStream()
    override val inputStream: InputStream

    private var isDead = false
    private var killJob : Job? = null

    init {
        outputStream.flush()
        inputStream = socket.getInputStream()
        startReceiving()
    }

    fun restartKillJob() {
        killJob?.cancel()

        if(!isDead) {
            killJob = GlobalScope.launch {
                delay(GameConstants.TIMEOUT_TIME * 1000L)
                logger.info("Failed to receive a heartbeat from: $id, forcefully closing their connection now!")
                disconnect()
            }
        }
    }


    override fun isConnected(): Boolean {
        return !isDead
    }

    fun disconnect() {
        server.remove(id)
        isDead = true
        try {
            socket.close()
        } catch (e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
        }
    }
}