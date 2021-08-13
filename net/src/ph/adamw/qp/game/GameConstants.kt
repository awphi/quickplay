package ph.adamw.qp.game

object GameConstants {
    // Tick stuff
    const val PESC_SIZE: Int = 32
    const val TICK_STEP = 1f / 64f

    // Calculated tick stuff
    const val TICK_STEP_MILLIS = (TICK_STEP * 1000L).toLong()

    // Phys
    const val WORLD_WIDTH = 1920f
    const val WORLD_HEIGHT = 1080f
    const val PPM = 24f

    // Connection management
    const val HEARTBEAT_PULSE = 5
    const val TIMEOUT_TIME = 10

    // Port
    const val DEFAULT_PORT = 3336
}