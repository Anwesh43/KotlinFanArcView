package ui.anwesome.com.kotlinstepfanview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.fanrotateview.FanRotateView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FanRotateView.create(this)
    }
}
