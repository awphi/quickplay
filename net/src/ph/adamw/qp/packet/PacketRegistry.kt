package ph.adamw.qp.packet

import org.reflections.Reflections
import ph.adamw.qp.GameManager
import ph.adamw.qp.net.packet.PacketType
import java.lang.RuntimeException

class PacketRegistry(private val manager: GameManager) {
    private val map = HashMap<PacketType, PacketHandler>()

    fun build() {
        val reflections = Reflections("ph.adamw.qp")
        val handlers = reflections.getTypesAnnotatedWith(PacketLink::class.java)
        for(i in handlers) {
            if(i.isAssignableFrom(PacketHandler::class.java)) {
                throw RuntimeException("PacketHandler annotation can only be applied to instances of IPacketHandler: $i")
            }

            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val pkt = i.getDeclaredAnnotation(PacketLink::class.java).packetType
            val ins = i.newInstance() as PacketHandler
            ins.manager = manager
            map[pkt] = ins
        }
    }

    fun getHandler(type: PacketType) : PacketHandler? {
        return map[type]
    }
}