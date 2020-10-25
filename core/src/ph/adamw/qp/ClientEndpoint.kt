package ph.adamw.qp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.packet.PacketType
import java.lang.Exception
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

object ClientEndpoint : Endpoint(QuickplayApplication.localManager) {
    var pid : Long = -2L

    public override var tcpSocket = Socket()
    override var udpSocket = DatagramSocket(null)

    private lateinit var heartbeat : Job

    fun connect(hostname: String, port: Int) : Boolean {
        if (isConnected()) {
            disconnectAndAlertServer()
        }

        try {
            tcpSocket.connect(InetSocketAddress(hostname, port), GameConstants.TIMEOUT_TIME * 1000)
            udpSocket.bind(tcpSocket.localSocketAddress)
            udpSocket.connect(tcpSocket.remoteSocketAddress)
        } catch(e: Exception) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }

        tcpSocket.outputStream.flush()

        startHeartBeat()
        startReceivingTcp()
        startReceivingUdp()
        return true
    }

    fun disconnectAndAlertServer() {
        sendTcp(PacketType.DISCONNECT_REQUEST)
        disconnect()
    }

    private fun disconnect() {
        if(::heartbeat.isInitialized) {
            heartbeat.cancel()
        }

        tcpSocket.close()
        udpSocket.close()

        tcpSocket = Socket()
        udpSocket = DatagramSocket(null)
    }

    override fun isConnected(): Boolean {
        return ::heartbeat.isInitialized && !heartbeat.isCancelled
    }

    private fun startHeartBeat() {
        heartbeat = GlobalScope.launch {
            while(sendTcp(PacketType.HEARTBEAT)) {
                delay(GameConstants.HEARTBEAT_PULSE * 1000L)
            }
        }
    }
}