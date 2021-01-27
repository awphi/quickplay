package ph.adamw.qp.game.component.util

import com.badlogic.ashley.core.Component
import com.google.gson.GsonBuilder
import org.reflections.Reflections
import ph.adamw.qp.io.RuntimeTypeAdapterFactory
import java.io.Serializable

object ComponentRegistry {
    private const val COMPONENT_LOCATION = "ph.adamw.qp.game.component"

    fun register(gsonBuilder: GsonBuilder) {
        val reflections = Reflections(COMPONENT_LOCATION)
        val rta = RuntimeTypeAdapterFactory.of(Component::class.java)
        val components = reflections.getSubTypesOf(Component::class.java)

        for(i in components) {
            if(i !is Serializable) {
                continue
            }

            rta.registerSubtype(i)
        }

        gsonBuilder.registerTypeAdapterFactory(rta)
    }
}