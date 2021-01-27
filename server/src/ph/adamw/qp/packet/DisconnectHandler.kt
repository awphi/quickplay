package ph.adamw.qp.packet

import com.google.gson.JsonElement
import mu.KotlinLogging
import ph.adamw.qp.Endpoint
import ph.adamw.qp.ServerEndpoint

@PacketLink(PacketType.DISCONNECT_REQUEST)
class DisconnectHandler : PacketHandler() {
    private val logger = KotlinLogging.logger {}

    override fun handle(data: JsonElement, from: Endpoint) {
        if(from !is ServerEndpoint) {
            return
        }

        from.disconnect()
    }
}