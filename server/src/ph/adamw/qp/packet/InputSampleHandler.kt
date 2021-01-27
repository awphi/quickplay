package ph.adamw.qp.packet

import com.badlogic.gdx.Input
import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.ServerEndpoint
import ph.adamw.qp.game.input.InputSnapshot
import ph.adamw.qp.io.JsonUtils

@PacketLink(PacketType.INPUT_SAMPLE)
class InputSampleHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        manager.setInput((from as ServerEndpoint).id, JsonUtils.fromJson(data, InputSnapshot::class.java))
    }
}