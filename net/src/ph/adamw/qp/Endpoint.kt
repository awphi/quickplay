package ph.adamw.qp

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import mu.KotlinLogging
import ph.adamw.qp.net.packet.PacketType
import ph.adamw.qp.io.JsonUtils
import java.io.*

abstract class Endpoint(private val manager: GameManager) {
    private val logger = KotlinLogging.logger {}

    protected abstract val outputStream: OutputStream
    protected abstract val inputStream: InputStream

    protected abstract fun isConnected(): Boolean

    protected fun startReceiving() {
        Thread({
            receive()
        }, "PktReceive").start()
    }

    fun send(type: PacketType): Boolean {
        return send(type, null)
    }

    fun send(type: PacketType, content: Any?): Boolean {
        if (!isConnected()) {
            return false
        }
        val parent = JsonObject()
        parent.addProperty("packet_id", type.getId())

        if (content is JsonElement) {
            parent.add("data", content)
        } else {
            parent.add("data", JsonUtils.toJsonTree(content))
        }

        logger.debug("Dispatching " + type + ": " + content.toString())

        try {
            outputStream.write((parent.toString() + "\n").toByteArray())
        } catch (e: IOException) {
            logger.trace(e.localizedMessage, e.cause)
            return false
        }
        return true
    }

    private fun receive() {
        val br = BufferedReader(InputStreamReader(inputStream))
        while (isConnected()) {
            try {
                if (!br.ready()) {
                    continue
                }
                val content = br.readLine()

                // String is split here as sometimes messages can stack up due to latency and this avoids us trying to parse multiple json
                // objects as one.
                val split = content.split("\n".toRegex()).toTypedArray()

                for (i in split) {
                    if (i == "") {
                        continue
                    }

                    val json: JsonElement
                    json = try {
                        JsonUtils.parseJson(i)
                    } catch (e: JsonSyntaxException) {
                        logger.trace(e.localizedMessage, e.cause)
                        continue
                    }

                    if (!json.isJsonObject) {
                        continue
                    }
                    val obj: JsonObject = json.asJsonObject
                    val id: JsonElement = obj.get("packet_id")

                    if (!id.isJsonPrimitive) {
                        continue
                    }

                    val idAsInt: Int
                    idAsInt = try {
                        id.asInt
                    } catch (e: NumberFormatException) {
                        logger.trace(e.localizedMessage, e.cause)
                        continue
                    }

                    val pkt = PacketType.getPacket(idAsInt) ?: continue
                    val handler = manager.packetRegistry.getHandler(pkt) ?: continue
                    logger.debug("Handling $pkt: $content")
                    handler.handle(obj.get("data"), this)
                }
            } catch (e: IOException) {
                logger.trace(e.localizedMessage, e.cause)
            }
        }
    }
}