package ph.adamw.qp.packet

enum class PacketType {
    GAME_UPDATE,
    HEARTBEAT,
    PID_ASSIGN,
    DISCONNECT_REQUEST;

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