package ph.adamw.qp.packet

import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.io.JsonUtils


@PacketLink(PacketType.GAME_SET)
class GameSetHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        QuickplayApplication.localManager.init(JsonUtils.fromJson(data, AbstractGame::class.java))
    }
}