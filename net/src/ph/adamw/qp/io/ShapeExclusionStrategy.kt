package ph.adamw.qp.io

import com.badlogic.gdx.physics.box2d.Shape
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class ShapeExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.declaringClass.isAssignableFrom(Shape::class.java) && f.name == "addr"
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}