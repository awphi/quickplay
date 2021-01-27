package ph.adamw.qp.packet

import com.badlogic.ashley.core.Entity
import com.google.gson.JsonElement
import ph.adamw.qp.Endpoint
import ph.adamw.qp.game.EntityUtils
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.game.component.util.Mappers
import ph.adamw.qp.io.JsonUtils

@PacketLink(PacketType.ENTITY_UPDATE)
class EntityUpdateHandler : PacketHandler() {
    override fun handle(data: JsonElement, from: Endpoint) {
        val entity = JsonUtils.fromJson(data, Entity::class.java)
        val id = Mappers.get(IDComponent::class.java).get(entity).id
        val localEntity = manager.getEntity(id) ?: return

        EntityUtils.copyEntityInto(entity, localEntity)
    }
}