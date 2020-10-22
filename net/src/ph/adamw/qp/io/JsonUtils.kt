package ph.adamw.qp.io

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.*
import com.google.gson.*
import ph.adamw.qp.game.AbstractGame
import ph.adamw.qp.pong.PongGame
import ph.adamw.qp.game.component.*
import ph.adamw.qp.game.input.InputHandler
import ph.adamw.qp.pong.PongInputHandler


object JsonUtils {
    val gson : Gson

    init {
        val builder = GsonBuilder()

        // TODO switch gameRta and inputHandlerRta to reflective/declarative registry
        val gameRta = RuntimeTypeAdapterFactory.of(AbstractGame::class.java)
                .registerSubtype(PongGame::class.java)

        val inputHandlerRta = RuntimeTypeAdapterFactory.of(InputHandler::class.java)
                .registerSubtype(PongInputHandler::class.java)

        val shapeRta = RuntimeTypeAdapterFactory.of(Shape::class.java)
                .registerSubtype(CircleShape::class.java)
                .registerSubtype(PolygonShape::class.java)

        builder.registerTypeAdapter(Entity::class.java, EntityTypeAdapter())
        builder.registerTypeAdapter(CircleShape::class.java, CircleShapeTypeAdapter())
        builder.registerTypeAdapter(PolygonShape::class.java, PolygonShapeTypeAdapter())

        builder.registerTypeAdapterFactory(gameRta)
        builder.registerTypeAdapterFactory(shapeRta)
        builder.registerTypeAdapterFactory(inputHandlerRta)
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
