package ph.adamw.qp.packet

import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.net.packet.PacketType
import ph.adamw.qp.io.JsonUtils


@PacketLink(PacketType.CONN_ACCEPT)
class ConnectionAcceptedHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        QuickplayApplication.localManager.init(JsonUtils.fromJson(data, AbstractGame::class.java))
        QuickplayApplication.localManager.getGame().onConnect(GameConstants.LOCAL_PID)
    }
}