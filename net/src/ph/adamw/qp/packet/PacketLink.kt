package ph.adamw.qp.packet

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class PacketLink(val packetType: PacketType)