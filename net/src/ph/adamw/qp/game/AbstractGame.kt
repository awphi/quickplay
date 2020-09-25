package ph.adamw.qp.game

abstract class AbstractGame {
    abstract val name : String
    abstract val minPlayers: Int
    abstract val maxPlayers: Int
}