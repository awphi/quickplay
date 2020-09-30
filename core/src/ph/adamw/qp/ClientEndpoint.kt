package ph.adamw.qp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.net.packet.PacketType
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

object ClientEndpoint : Endpoint(QuickplayApplication.localManager) {
    private val logger = KotlinLogging.logger {}
    private var socket = Socket()

    override lateinit var outputStream: OutputStream
    override lateinit var inputStream: InputStream

    private lateinit var heartbeat : Job

    fun connect(hostname: String?, port: Int) {
        if (isConnected()) {
            disconnectAndAlertServer()
        }

        socket.connect(InetSocketAddress(hostname, port), GameConstants.TIMEOUT_TIME * 1000)
        outputStream = socket.getOutputStream()
        outputStream.flush()
        inputStream = socket.getInputStream()

        startHeartBeat()
        startReceiving()
    }

    fun disconnectAndAlertServer() {
        send(PacketType.DISCONNECT)
        disconnect()
    }

    fun attemptConnect(host: String?, port: Int): Boolean {
        try {
            connect(host, port)
        } catch (e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }

        return true
    }

    private fun disconnect() {
        heartbeat.cancel()

        try {
            socket.close()
            socket = Socket()
        } catch (e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
        }
    }

    override fun isConnected(): Boolean {
        return ::heartbeat.isInitialized && !heartbeat.isCancelled
    }

    private fun startHeartBeat() {
        heartbeat = GlobalScope.launch {
            while(send(PacketType.HEARTBEAT)) {
                delay(GameConstants.HEARTBEAT_PULSE * 1000L)
            }
        }
    }
}