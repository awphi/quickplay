package ph.adamw.qp

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.jayway.jsonpath.JsonPath
import mu.KotlinLogging
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.ValueMatcher
import org.skyscreamer.jsonassert.comparator.CustomComparator
import java.lang.AssertionError
import kotlin.math.abs


object ComponentComparator {
    private val logger = KotlinLogging.logger {}

    private val rulesMap = HashMap<String, Array<Customization>>()

    init {
        val looseFloatComparator = fun(o1: Any?, o2: Any?) : Boolean {
            if (o1 !is Number || o2 !is Number) {
                return false
            }

            return abs(o1 as Float - o2 as Float) < 0.1f
        }

        // TODO move this somewhere neater, this is fine for now, perhaps annotations & reflection again
        rulesMap["PhysicsComponent"] = arrayOf(
            Customization("$.bodyData.position.x", looseFloatComparator),
            Customization("$.bodyData.position.y", looseFloatComparator)
        )
    }

    fun compare(e1: JsonObject, e2: JsonObject, loose: Boolean) : Boolean {
        try {
            val customizations = rulesMap[e1.get("type").asString]

            if(loose && customizations != null) {
                JSONAssert.assertEquals(
                    e1.toString(), e2.toString(),
                    CustomComparator(JSONCompareMode.STRICT_ORDER, *customizations)
                )
            } else {
                JSONAssert.assertEquals(e1.toString(), e1.toString(), JSONCompareMode.STRICT)
            }
        } catch (e: AssertionError) {
            return false
        }

        return true
    }
}