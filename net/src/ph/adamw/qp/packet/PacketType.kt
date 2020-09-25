package ph.adamw.qp.net.packet

enum class PacketType {
    CONN_ACCEPT,
    HEARTBEAT,
    PLAYER_POSITION,
    DISCONNECT;

    fun getId(): Int {
        return ordinal
    }

    companion object {
        private val values: Array<PacketType> = values()

        fun getPacket(id: Int): PacketType? {
            return if (id < 0 || id >= values.size) {
                null
            } else values[id]
        }
    }
}