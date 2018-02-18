package ui.anwesome.com.fanrotateview

/**
 * Created by anweshmishra on 19/02/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
class FanRotateView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class Animator(var view:View, var animated:Boolean = false) {
        fun animate(updatecb: () -> Unit) {
            if(animated) {
                try {
                    updatecb()
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class State(var n:Int, var prevScale: Float = 0f, var dir:Float = 0f, var j:Int = 0, var jDir:Int = 1) {
        var scale:Float = 0f
        fun update(stopcb: (Float, Int) -> Unit) {
            scale += dir * 0.1f
            if(Math.abs(scale - prevScale) > 1) {
                j += jDir
                scale = prevScale + dir
                dir = 0f
                stopcb(scale, j)
                if(j == n || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale = scale
                }
                else {
                    scale = prevScale
                }
                stopcb(scale, j)
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1f - 2*scale 
                startcb()
            }
        }
    }
}