package ph.adamw.qp.packet

import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.ServerEndpoint
import ph.adamw.qp.net.packet.PacketType

@PacketLink(PacketType.HEARTBEAT)
class HeartbeatHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        (from as ServerEndpoint).restartKillJob()
    }
}