package ph.adamw.qp.io

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class PolygonShapeTypeAdapter : TypeAdapter<PolygonShape>() {
    override fun write(w: JsonWriter, value: PolygonShape) {
        val vec2Adapter = JsonUtils.getAdapter(Vector2::class.java)

        w.beginObject()
        w.name("verts")
        w.beginArray()
        val vec = Vector2()
        for(i in 0 until value.vertexCount) {
            value.getVertex(i, vec)
            vec2Adapter.write(w, vec)
        }
        w.endArray()
        w.endObject()
    }

    override fun read(r: JsonReader): PolygonShape {
        val vec2Adapter = JsonUtils.getAdapter(Vector2::class.java)
        val verts = ArrayList<Vector2>()

        r.beginObject()
        while(r.hasNext()) {
            if(r.nextName() == "verts") {
                r.beginArray()
                while (r.hasNext()) {
                    verts.add(vec2Adapter.read(r))
                }
                r.endArray()
            }
        }
        r.endObject()

        val shape = PolygonShape()
        shape.set(verts.toTypedArray())
        return shape
    }
}