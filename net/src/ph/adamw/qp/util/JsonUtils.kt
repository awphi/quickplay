package ph.adamw.qp.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.PongGame


object JsonUtils {
    private val gson : Gson

    init {
        val gameRta = RuntimeTypeAdapterFactory.of(AbstractGame::class.java)
                .registerSubtype(PongGame::class.java)

        gson = GsonBuilder().registerTypeAdapterFactory(gameRta).create()
    }

    fun parseJson(y: String): JsonElement {
        return JsonParser.parseString(y)
    }

    fun <T> toJson(o: Any, clazz: Class<T>): JsonElement {
        return gson.toJsonTree(o, clazz)
    }

    fun toJson(o: Any?): JsonElement {
        return gson.toJsonTree(o)
    }

    fun <T> fromJson(element: JsonElement?, clazz: Class<T>?): T {
        return gson.fromJson(element, clazz)
    }
}
