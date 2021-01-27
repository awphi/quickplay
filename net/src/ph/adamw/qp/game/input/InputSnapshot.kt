package ph.adamw.qp.game.input

import com.badlogic.gdx.InputAdapter

class InputSnapshot : InputAdapter() {
    val keysDown = HashSet<Int>()

    override fun keyDown(keycode: Int): Boolean {
        keysDown.add(keycode)
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        keysDown.remove(keycode)
        return true
    }

    fun isKeyDown(key: Int) : Boolean {
        return key in keysDown
    }
}