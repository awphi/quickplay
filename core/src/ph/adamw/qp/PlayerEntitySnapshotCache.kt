package ph.adamw.qp

import com.badlogic.gdx.utils.Queue
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import mu.KotlinLogging

class PlayerEntitySnapshotCache(private val size: Int) {
    private val queue = Queue<JsonElement>(size)

    private val logger = KotlinLogging.logger {}

    fun add(snap: JsonElement) {
        //logger.debug { "Adding snapshot to PESC: $snap ${queue.size}" }
        queue.addFirst(snap)

        if(queue.size == size) {
            queue.removeLast()
        }
    }

    fun contains(snap: JsonElement, loose: Boolean) : Boolean {
        val it = queue.iterator()
        val snapArr = snap as JsonArray
        val sn = snapArr.size()

        while(it.hasNext()) {
            val cached = it.next()

            // First check with strict equality
            if(cached.equals(snap)) {
                return true
            }

            val cachedArr = cached as JsonArray

            // Mismatching number of components
            if(sn != cachedArr.size()) {
                continue
            }

            var c = 0
            for (i in 0 until sn) {
                val cacheJson = cachedArr.get(i).asJsonObject
                val snapJson = snapArr.get(i).asJsonObject

                if(ComponentComparator.compare(cacheJson, snapJson, loose)) {
                    c += 1
                } else {
                    break
                }
            }

            if(c == sn) {
                return true
            }
        }


        return false
    }
}