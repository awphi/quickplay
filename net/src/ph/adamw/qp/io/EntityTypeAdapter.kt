package ph.adamw.qp.io

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.Serializable

class EntityTypeAdapter : TypeAdapter<Entity>() {
    override fun write(w: JsonWriter, value: Entity) {
        w.beginArray()
        for(i in value.components) {
            if(i !is Serializable) {
                continue
            }

            val adapter = JsonUtils.getAdapter(i.javaClass)
            adapter.write(w, i)
        }
        w.endArray()
    }

    override fun read(r: JsonReader): Entity {
        val entity = Entity()
        r.beginArray()
        while(r.hasNext()) {
            val adapter = JsonUtils.getAdapter(Component::class.java)
            entity.add(adapter.read(r))
        }
        r.endArray()
        return entity
    }
}