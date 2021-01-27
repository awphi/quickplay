package ph.adamw.qp.game.system

import com.badlogic.ashley.systems.IntervalSystem
import ph.adamw.qp.ClientEndpoint
import ph.adamw.qp.QuickplayApplication
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.game.input.InputSnapshot
import ph.adamw.qp.packet.PacketType

// TODO write serializer & deserializer for gdx's Input interface
class InputSampleSystem(private val inputSnapshot: InputSnapshot) : IntervalSystem(GameConstants.TICK_STEP, -1) {
    override fun updateInterval() {
        ClientEndpoint.sendUdp(PacketType.INPUT_SAMPLE, inputSnapshot)
        QuickplayApplication.localManager.setInput(ClientEndpoint.pid, inputSnapshot)
    }
}