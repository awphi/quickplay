package ph.adamw.qp.packet

import ph.adamw.qp.net.packet.PacketType

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PacketLink(val packetType: PacketType)