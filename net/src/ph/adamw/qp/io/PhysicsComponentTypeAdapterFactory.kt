package ph.adamw.qp.io

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import ph.adamw.qp.game.component.BodyData
import ph.adamw.qp.game.component.PhysicsComponent

class PhysicsComponentTypeAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>?): TypeAdapter<T>? {
        if(type == null || !type.rawType.isAssignableFrom(PhysicsComponent::class.java)) {
            return null
        }

        val delegate = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            override fun write(w: JsonWriter, value: T) {
                val component = (value as PhysicsComponent)
                component.createBodyData()
                delegate.write(w, value)
            }

            override fun read(r: JsonReader): T {
                return delegate.read(r)
            }
        }
    }
}