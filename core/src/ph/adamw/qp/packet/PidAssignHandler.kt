package ph.adamw.qp.packet

import com.google.gson.JsonElement
import ph.adamw.qp.ClientEndpoint
import ph.adamw.qp.Endpoint
import ph.adamw.qp.io.JsonUtils

@PacketLink(PacketType.PID_ASSIGN)
class PidAssignHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        ClientEndpoint.pid = JsonUtils.fromJson(data, Long::class.java)
    }
}