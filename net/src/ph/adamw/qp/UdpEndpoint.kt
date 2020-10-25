package ph.adamw.qp

import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.packet.PacketType
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket

abstract class UdpEndpoint(manager: GameManager) : Endpoint(manager) {
    protected abstract val udpSocket : DatagramSocket

    protected fun startReceivingUdp() {
        Thread({
            receiveUdp()
        }, "UDP-IN").start()
    }

    private fun receiveUdp() {
        val buf = ByteArray(65535)
        while (isConnected()) {
            val receive = DatagramPacket(buf, buf.size)
            udpSocket.receive(receive)
            val s = String(receive.data, 0, receive.length, Charsets.UTF_8)
            for(i in s.split("\n")) {
                receivePacket(i)
            }
        }
    }

    fun sendUdp(type: PacketType, content: Any?): Boolean {
        if (!isConnected()) {
            return false
        }

        val bytes = packet(type, content).toByteArray(Charsets.UTF_8)
        logger.debug("Dispatching UDP: $type: $content")

        val dp = DatagramPacket(bytes, 0, bytes.size, tcpSocket.inetAddress, GameConstants.UDP_PORT)

        try {
            udpSocket.send(dp)
        } catch(e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }

        return true
    }
}