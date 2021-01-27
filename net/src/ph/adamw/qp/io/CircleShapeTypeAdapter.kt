package ph.adamw.qp.io

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class CircleShapeTypeAdapter : TypeAdapter<CircleShape>() {
    override fun write(w: JsonWriter, value: CircleShape) {
        val vec2Adapter = JsonUtils.getAdapter(Vector2::class.java)

        w.beginObject()
        w.name("position")
        vec2Adapter.write(w, value.position)
        w.name("radius").value(value.radius)
        w.endObject()
    }

    override fun read(r: JsonReader): CircleShape {
        val vec2Adapter = JsonUtils.getAdapter(Vector2::class.java)
        val shape = CircleShape()

        r.beginObject()
        while(r.hasNext()) {
            when(r.nextName()) {
                "position" -> shape.position.set(vec2Adapter.read(r))
                "radius" -> shape.radius = r.nextDouble().toFloat()
            }
        }
        r.endObject()
        return shape
    }
}