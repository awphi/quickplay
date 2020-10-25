package ph.adamw.qp

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import mu.KotlinLogging
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.packet.PacketType
import ph.adamw.qp.io.JsonUtils
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Socket

abstract class Endpoint(private val manager: GameManager) {
    protected val logger = KotlinLogging.logger {}
    protected abstract val tcpSocket : Socket

    protected abstract fun isConnected(): Boolean

    protected fun startReceivingTcp() {
        Thread({
            receiveTcp()
        }, "TCP-IN").start()
    }

    fun sendTcp(type: PacketType): Boolean {
        return sendTcp(type, null)
    }

    fun sendTcp(type: PacketType, content: Any?): Boolean {
        if (!isConnected()) {
            return false
        }

        val pkt = packet(type, content)
        logger.debug("Dispatching TCP: $type: $content")

        try {
            tcpSocket.outputStream.write(pkt.toByteArray(Charsets.UTF_8))
        } catch (e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }

        return true
    }

    protected fun packet(type: PacketType, content: Any?) : String {
        val parent = JsonObject()
        parent.addProperty("packet_id", type.getId())

        if (content is JsonElement) {
            parent.add("data", content)
        } else {
            parent.add("data", JsonUtils.toJsonTree(content))
        }

        return parent.toString() + "\n"
    }

    protected fun receivePacket(content: String) {
        if (content == "") {
            return
        }

        val json = try {
            JsonUtils.parseJson(content)
        } catch (e: JsonSyntaxException) {
            logger.trace(e.localizedMessage, e.cause)
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
        logger.debug("Handling: $pkt: $data")
        handler.handle(data, this)
    }

    private fun receiveTcp() {
        val br = BufferedReader(InputStreamReader(tcpSocket.inputStream))
        while (isConnected()) {
            if (!br.ready()) {
                continue
            }

            receivePacket(br.readLine())
        }
    }
}