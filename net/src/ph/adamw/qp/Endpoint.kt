package ph.adamw.qp

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import mu.KotlinLogging
import ph.adamw.qp.packet.PacketType
import ph.adamw.qp.io.JsonUtils
import java.io.*
import java.net.*

abstract class Endpoint(private val manager: GameManager) {
    protected val logger = KotlinLogging.logger {}

    protected abstract val tcpSocket : Socket
    protected abstract val udpSocket : DatagramSocket

    protected abstract fun isConnected(): Boolean

    protected fun startReceivingUdp() {
        Thread({
            val buf = ByteArray(65535)
            while (isConnected()) {
                val receive = DatagramPacket(buf, buf.size)
                try {
                    udpSocket.receive(receive)
                } catch(ignored: SocketException) {}
                val s = String(receive.data, 0, receive.length, Charsets.UTF_8)
                for(i in s.split("\n")) {
                    handlePacket(i)
                }
            }
        }, "UDP-IN").start()
    }

    fun sendUdp(type: PacketType, content: Any?): Boolean {
        if (!isConnected()) {
            return false
        }

        val bytes = wrapPacket(type, content).toByteArray(Charsets.UTF_8)
        //logger.debug("Dispatching UDP: $type: $content")

        val dp = DatagramPacket(bytes, 0, bytes.size)

        try {
            udpSocket.send(dp)
        } catch(e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }

        return true
    }

    protected fun startReceivingTcp() {
        Thread({
            val br = BufferedReader(InputStreamReader(tcpSocket.inputStream))
            while (isConnected()) {
                if (!br.ready()) {
                    continue
                }

                handlePacket(br.readLine())
            }
        }, "TCP-IN").start()
    }

    fun sendTcp(type: PacketType): Boolean {
        return sendTcp(type, null)
    }

    fun sendTcp(type: PacketType, content: Any?): Boolean {
        if (!isConnected()) {
            return false
        }

        val pkt = wrapPacket(type, content)
        //logger.debug("Dispatching TCP: $type: $content")

        try {
            tcpSocket.outputStream.write(pkt.toByteArray(Charsets.UTF_8))
        } catch (e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }

        return true
    }

    protected fun wrapPacket(type: PacketType, content: Any?) : String {
        val parent = JsonObject()
        parent.addProperty("packet_id", type.getId())

        if (content is JsonElement) {
            parent.add("data", content)
        } else {
            parent.add("data", JsonUtils.toJsonTree(content))
        }

        return parent.toString() + "\n"
    }

    private fun handlePacket(content: String) {
        if (content == "") {
            return
        }

        val json = try {
            JsonUtils.parseJson(content)
        } catch (e: JsonSyntaxException) {
            logger.trace(e.localizedMessage, e.cause)
            logger.trace(content)
            return
        }

        if (!json.isJsonObject) {
            return
        }
        val obj: JsonObject = json.asJsonObject
        val id: JsonElement = obj.get("packet_id")

        if (!id.isJsonPrimitive) {
            return
        }

        val idAsInt= try {
            id.asInt
        } catch (e: NumberFormatException) {
            logger.trace(e.localizedMessage, e.cause)
            return
        }

        val pkt = PacketType.getPacket(idAsInt) ?: return
        val handler = manager.packetRegistry.getHandler(pkt) ?: return
        val data = obj.get("data")

        handler.handle(data, this)
        //logger.debug("Handling: $pkt: $data")
    }
}