package ph.adamw.qp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ph.adamw.qp.game.GameConstants
import java.net.DatagramSocket
import java.net.Socket

class ServerEndpoint(val id: Long, private val server: GameServer, override val tcpSocket: Socket) : Endpoint(server.manager) {
    private var isDead = false
    private var killJob : Job? = null

    override val udpSocket = DatagramSocket(tcpSocket.localSocketAddress)

    init {
        tcpSocket.outputStream.flush()
        udpSocket.connect(tcpSocket.remoteSocketAddress)
        startReceivingTcp()
        startReceivingUdp()
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
        server.manager.getGame().onDisconnect(id)
        server.remove(id)
        isDead = true
        killJob?.cancel()
        logger.info("Disconnecting: $id")

        tcpSocket.close()
        udpSocket.close()
    }
}