package ph.adamw.qp

import com.badlogic.ashley.core.Entity
import com.google.gson.JsonElement
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.game.component.PlayerComponent
import ph.adamw.qp.game.component.util.Mappers
import ph.adamw.qp.game.input.InputSnapshot
import ph.adamw.qp.io.JsonUtils

class ClientGameManager : GameManager() {
    private val playerEntitySnapshotCaches = HashMap<Long, PlayerEntitySnapshotCache>()
    private val idMapper = Mappers.get(IDComponent::class.java)

    private fun addPlayerEntitySnapshot(id: Long, snap: JsonElement) {
        if(id !in playerEntitySnapshotCaches) {
            playerEntitySnapshotCaches[id] = PlayerEntitySnapshotCache(GameConstants.PESC_SIZE)
        }

        playerEntitySnapshotCaches[id]!!.add(snap)
    }

    fun getPlayerEntitySnapshotCache(id: Long) : PlayerEntitySnapshotCache? {
        return playerEntitySnapshotCaches[id]
    }

    override fun onEntityInputHandled(input: InputSnapshot, entity: Entity) {
        addPlayerEntitySnapshot(idMapper.get(entity).id, JsonUtils.toJsonTree(entity))
    }
}