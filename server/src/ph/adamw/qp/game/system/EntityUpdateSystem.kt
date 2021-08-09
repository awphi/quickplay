package ph.adamw.qp.game.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ph.adamw.qp.GameServer
import ph.adamw.qp.ServerEndpoint
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.packet.PacketType

class EntityUpdateSystem(private val server: GameServer) : IntervalIteratingSystem(Family.all(IDComponent::class.java).get(), GameConstants.TICK_STEP) {
    private val updateBuffer = HashMap<Long, HashSet<Entity>>()

    fun addToBuffer(id: Long, entity: Entity) {
        if(!updateBuffer.containsKey(id)) {
            updateBuffer[id] = HashSet()
        }

        updateBuffer[id]!!.add(entity)
    }

    fun purgeBuffer(id: Long) {
        updateBuffer[id]?.clear()
    }

    override fun processEntity(entity: Entity) {
        for(i : Long in server.getConnections()) {
            // TODO:
            //  check if this given user needs this given entity updating, if not don't add to buffer
            addToBuffer(i, entity)
        }
    }

    override fun updateInterval() {
        super.updateInterval()
        for(i : Long in updateBuffer.keys) {
            for(j : Entity in updateBuffer[i]!!) {
                server.get(i)?.sendUdp(PacketType.ENTITY_UPDATE, j)
            }

            purgeBuffer(i)
        }
    }
}