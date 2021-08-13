package ph.adamw.qp.packet

import com.badlogic.ashley.core.Entity
import com.google.gson.JsonElement
import mu.KotlinLogging
import ph.adamw.qp.ClientEndpoint
import ph.adamw.qp.Endpoint
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.entity.EntityUtils
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

    private val logger = KotlinLogging.logger {}

    override fun handle(data: JsonElement, from: Endpoint) {
        val entity = JsonUtils.fromJson(data, Entity::class.java)
        val id = idMapper.get(entity).id
        val localEntity = manager.getEntity(id) ?: return

        val playerComponent = playerMapper.get(entity)
        val echoedComponent = echoMapper.get(localEntity)

        // I.e. if the entity is controlled by us, don't listen to it verbatim instead use predictive alleviation
        if (playerComponent != null && playerComponent.owner == ClientEndpoint.pid && playerComponent.inputHandler != null) {
            if(echoedComponent == null) {
                //EchoedComponent.addEcho(localEntity)
            }

            val pesc = QuickplayApplication.localManager.getPlayerEntitySnapshotCache(id)
            val snap = JsonUtils.toJsonTree(entity);
            if(pesc != null && !pesc.contains2(snap)) {
                EntityUtils.copyEntityInto(entity, localEntity, false)
                //pesc.add(snap)
            }

        } else {
            EntityUtils.copyEntityInto(entity, localEntity, false)
        }

        if(echoedComponent != null) {
            EntityUtils.copyEntityInto(entity, echoedComponent.echo, false)
        }
    }
}