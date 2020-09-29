package ph.adamw.qp.packet

import com.badlogic.gdx.utils.JsonValue
import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.net.packet.PacketType
import ph.adamw.qp.util.JsonUtils


@PacketLink(PacketType.CONN_ACCEPT)
class ConnectionAcceptedHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        QuickplayApplication.localManager.init(JsonUtils.fromJson(data, AbstractGame::class.java))
        QuickplayApplication.localManager.getGame().onConnect(QuickplayApplication.LOCAL_PID)
    }
}