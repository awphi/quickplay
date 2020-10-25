package ph.adamw.qp

import mu.KotlinLogging
import ph.adamw.qp.game.GameConstants
import ph.adamw.qp.packet.PacketType
import java.net.DatagramSocket
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.collections.HashSet

class GameServer {
    private val map = TreeMap<Long, ServerEndpoint>()
    private val logger = KotlinLogging.logger {}
    val manager = GameManager()

    fun listenForConnections(port : Int) {
        val socket = ServerSocket(port)
        Thread({
            while (!socket.isClosed) {
                if (manager.getGame().maxPlayers > getConnected()) {
                    val conn = socket.accept()
                    add(conn)
                }
            }
        }, "ConnAccept").start()
        logger.info("Server listening on port: $port")
    }

    fun run() {
        Thread({
            while(true) {
                manager.tick(GameConstants.TICK_STEP)
                Thread.sleep(GameConstants.TICK_STEP_MILLIS)
            }
        }, "Game").start()
    }

    fun get(id: Long) : ServerEndpoint? {
        return map[id]
    }

    fun getConnected() : Int {
        return map.values.size
    }

    private fun getFreshPid() : Long {
        if (map.keys.size >= manager.getGame().maxPlayers) {
            return -1
        }
        var id: Long
        do {
            id = UUID.randomUUID().leastSignificantBits
        } while (map.keys.contains(id))
        return id
    }

    private fun broadcast(type: PacketType, content: Any?, to: Collection<ServerEndpoint>) {
        for (i : ServerEndpoint in to) {
            i.sendTcp(type, content)
        }
    }

    fun broadcast(type: PacketType, content: Any?) {
        broadcast(type, content, map.values)
    }

    fun broadcast(type: PacketType, content: Any?, vararg exclude: ServerEndpoint) {
        broadcast(type, content, map.values.filter { it !in exclude })
    }

    fun add(conn: Socket): ServerEndpoint {
        val clientId = getFreshPid()

        if (clientId == -1L) {
            throw RuntimeException("All client IDs are occupied! Did you make sure the pool is not full when adding a new connection?")
        }

        logger.info("Received connection from: ${conn.inetAddress}, assigned ID: $clientId")
        val client = ServerEndpoint(clientId, this, conn)
        map[clientId] = client
        client.restartKillJob()
        client.sendTcp(PacketType.PID_ASSIGN, clientId)
        manager.getGame().onConnect(clientId)
        client.sendTcp(PacketType.GAME_SET, manager.getGame())
        return client
    }

    fun remove(id: Long): Boolean {
        return map.remove(id) != null
    }
}
