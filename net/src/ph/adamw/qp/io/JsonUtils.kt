package ph.adamw.qp.io

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.google.gson.*
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.PongGame
import ph.adamw.qp.game.component.IDComponent
import ph.adamw.qp.game.component.NameComponent
import ph.adamw.qp.game.component.PhysicsComponent


object JsonUtils {
    private val gson : Gson

    init {
        /* TODO move these somewhere neater + more obvious - registries or reflectively generate */
        val gameRta = RuntimeTypeAdapterFactory.of(AbstractGame::class.java)
                .registerSubtype(PongGame::class.java)

        val componentRta = RuntimeTypeAdapterFactory.of(Component::class.java)
                .registerSubtype(NameComponent::class.java)
                .registerSubtype(PhysicsComponent::class.java)
                .registerSubtype(IDComponent::class.java)
        /* --- */

        gson = GsonBuilder()
                .registerTypeAdapterFactory(gameRta)
                .registerTypeAdapterFactory(componentRta)
                //.registerTypeAdapter(PhysicsComponent::class.java, PhysicsComponentTypeAdapter())
                .registerTypeAdapter(Entity::class.java, EntityTypeAdapter())
                .create()
    }

    fun parseJson(y: String): JsonElement {
        return JsonParser.parseString(y)
    }

    fun <T> getAdapter(type: Class<T>) : TypeAdapter<T> {
        return gson.getAdapter(type)
    }

    fun toJsonTree(o: Any?): JsonElement {
        return gson.toJsonTree(o)
    }

    fun <T> fromJson(element: JsonElement?, clazz: Class<T>?): T {
        return gson.fromJson(element, clazz)
    }
}