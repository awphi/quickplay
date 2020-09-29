package ph.adamw.qp.packet

import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.GameManager

abstract class PacketHandler {
    lateinit var manager: GameManager

    abstract fun handle(data: JsonElement, from: Endpoint)
}