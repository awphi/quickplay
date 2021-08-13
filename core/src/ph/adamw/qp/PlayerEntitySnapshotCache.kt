package ph.adamw.qp

import com.badlogic.gdx.utils.Queue
import com.google.common.collect.MapDifference
import com.google.common.collect.Maps
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import ph.adamw.qp.io.JsonUtils
import java.lang.reflect.Type

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

    fun contains(snap: JsonElement) : Boolean {
        return queue.indexOf(snap, false) != -1
    }

    fun contains2(snap: JsonElement) : Boolean {
        val it = queue.iterator()
        val diffs = HashSet<MapDifference<String, Any>>()
        while(it.hasNext()) {
            val snapArr = snap as JsonArray
            val cached = it.next() as JsonArray
            var c = 0
            for (i in 0 until snapArr.size()) {
                val mapType: Type = object : TypeToken<Map<String?, Any?>?>() {}.type
                val oCached = cached.get(i)
                val oSnap = snapArr.get(i)
                val firstMap: Map<String, Any> = JsonUtils.gson.fromJson(oCached, mapType)
                val secondMap: Map<String, Any> = JsonUtils.gson.fromJson(oSnap, mapType)
                val d = Maps.difference(firstMap, secondMap)

                // TODO change this areEqual to use some method that accounts for a margin of error for different props
                //  i.e. bodyData.position.x and bodyData.position.y can be +- ~0.1
                //  then replace the rounding in createBodyData
                if(d.areEqual()) {
                    c += 1
                } else {
                    diffs.add(d)
                }
            }

            if(c == snapArr.size()) {
                return true
            }
        }


        return false
    }
}