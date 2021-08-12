package ph.adamw.qp.packet

import com.badlogic.ashley.core.Entity
import com.google.gson.JsonElement
import ph.adamw.qp.ClientEndpoint
import ph.adamw.qp.Endpoint
import ph.adamw.qp.game.EntityUtils
import ph.adamw.qp.game.component.EchoedComponent
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.game.component.PlayerComponent
import ph.adamw.qp.game.component.util.Mappers
import ph.adamw.qp.io.JsonUtils

@PacketLink(PacketType.ENTITY_UPDATE)
class EntityUpdateHandler : PacketHandler() {
    private val idMapper =  Mappers.get(IDComponent::class.java);
    private val playerMapper = Mappers.get(PlayerComponent::class.java)
    private val echoMapper = Mappers.get(EchoedComponent::class.java)

    override fun handle(data: JsonElement, from: Endpoint) {
        val entity = JsonUtils.fromJson(data, Entity::class.java)
        val id = idMapper.get(entity).id
        val localEntity = manager.getEntity(id) ?: return

        val playerComponent = playerMapper.get(entity)
        val echoedComponent = echoMapper.get(localEntity)

        // I.e. if the entity is controlled by us, don't listen to it verbatim instead use predictive alleviation
        if (playerComponent != null && playerComponent.owner == ClientEndpoint.pid && playerComponent.inputHandler != null) {
            if(echoedComponent == null) {
                EchoedComponent.addEcho(localEntity)
            }
            // TODO see notes about predicted entity buffer,
        } else {
            EntityUtils.copyEntityInto(entity, localEntity, false)
        }

        if(echoedComponent != null) {
            EntityUtils.copyEntityInto(entity, echoedComponent.echo, false)
        }
    }
}