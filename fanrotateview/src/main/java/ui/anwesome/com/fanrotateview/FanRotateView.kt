package ui.anwesome.com.fanrotateview

/**
 * Created by anweshmishra on 19/02/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
class FanRotateView(ctx:Context, var n:Int = 4):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
                scale = prevScale + dir
                stopcb(scale, j)
                j += jDir
                dir = 0f
                if(j == n || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale = scale
                }
                else {
                    scale = prevScale
                }

            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1f - 2*scale
                startcb()
            }
        }
    }
    data class FanRotate(var w:Float, var h: Float, var n:Int) {
        val deg:Float = 360f/(2*n)
        val state = State(n)
        fun draw(canvas : Canvas, paint : Paint) {
            paint.color = Color.parseColor("#1565C0")
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.rotate(90f)
            val size = 2*Math.min(w,h)/3
            val sweep = state.j * deg + deg * state.scale
            canvas.drawArc(RectF(-size/2, -size/2, -size/2, -size/2), -sweep, 2 * sweep, true, paint)
            canvas.restore()
        }
        fun update(stopcb : (Float, Int) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view: FanRotateView, var time: Int = 0) {
        val animator = Animator(view)
        var fanRotate: FanRotate ?= null
        fun render(canvas:Canvas, paint: Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                if(view.n > 0) {
                    fanRotate = FanRotate(w, h, view.n)
                }
            }
            canvas.drawColor(Color.parseColor("#212121"))
            fanRotate?.draw(canvas, paint)
            time++
            animator.animate {
                fanRotate?.update {scale, j ->
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            fanRotate?.startUpdating {
                animator.start()
            }
        }
    }
}