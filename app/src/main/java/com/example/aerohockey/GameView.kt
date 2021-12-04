package com.example.aerohockey

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController


public class  GameView(context: Context?, attributeSet: AttributeSet?) : SurfaceView(context),
    SurfaceHolder.Callback {

    var field:Int=0
    var striker:Int=0
    var puck:Int=0
//    constructor(context: Context?, attributeSet: AttributeSet?,settings: Settings) {
//        this.field = Settings.field
//        this.striker = Settings.striker
//        this.puck = Settings.puck
//    }
    private var score:Int = 0
    private var drawThread: DrawThread? = null
    override fun surfaceCreated(holder: SurfaceHolder) {
        drawThread = DrawThread(context, getHolder(),Settings.field,Settings.striker,Settings.puck)
        drawThread!!.start()
    }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawThread!!.requestStop()
        var retry = true
        while (retry) {
            try {
                drawThread!!.join()
                retry = false
            } catch (e: InterruptedException) {
                //
            }
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        drawThread!!.setStrikerPoint(event.x.toInt(), event.y.toInt())
        return false
    }

    fun pause() {
        drawThread?.requestStop()
    }
    fun resume(){
        drawThread?.requestStart()
    }
    init {
        holder.addCallback(this)
    }
//    fun returnScore(score:Int){
//        val intent = Intent(getActivity(this.context), MainActivity::class.java)
//    }
}