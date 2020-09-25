package ph.adamw.qp.packet

import com.badlogic.gdx.utils.JsonValue
import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.ServerEndpoint
import ph.adamw.qp.net.packet.PacketType

@PacketLink(PacketType.DISCONNECT)
class DisconnectHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        (from as ServerEndpoint).disconnect()
    }
}