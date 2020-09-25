package ph.adamw.qp.packet.handle

import com.badlogic.gdx.utils.JsonValue
import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.packet.PacketHandler
import ph.adamw.qp.packet.PacketLink
import ph.adamw.qp.net.packet.PacketType

@PacketLink(PacketType.PLAYER_POSITION)
class PlayerPositionHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        if(manager.isHost) {
            // TODO validate the move and broadcast the validated move to all clients excluding from
        } else {
            //TODO Reflect the move on the game verbatim, no sanitisation
        }
    }
}