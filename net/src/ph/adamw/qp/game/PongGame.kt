package ph.adamw.qp.game

class PongGame : AbstractGame() {
    override val name: String
        get() = "Pong"
    override val minPlayers: Int
        get() = 2
    override val maxPlayers: Int
        get() = 2
}