package ph.adamw.qp.io

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.*
import com.google.gson.*
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.game.PongGame
import ph.adamw.qp.game.component.*


object JsonUtils {
    val gson : Gson

    init {
        val builder = GsonBuilder()

        val gameRta = RuntimeTypeAdapterFactory.of(AbstractGame::class.java)
                .registerSubtype(PongGame::class.java)

        val shapeRta = RuntimeTypeAdapterFactory.of(Shape::class.java)
                .registerSubtype(CircleShape::class.java)
                .registerSubtype(PolygonShape::class.java)

        builder.registerTypeAdapter(Entity::class.java, EntityTypeAdapter())
        builder.registerTypeAdapter(CircleShape::class.java, CircleShapeTypeAdapter())
        builder.registerTypeAdapter(PolygonShape::class.java, PolygonShapeTypeAdapter())

        builder.registerTypeAdapterFactory(gameRta)
        builder.registerTypeAdapterFactory(shapeRta)
        builder.registerTypeAdapterFactory(PhysicsComponentTypeAdapterFactory())

        ComponentRegistry.register(builder)

        gson = builder.create()
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
