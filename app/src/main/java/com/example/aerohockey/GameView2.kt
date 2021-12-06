package com.example.aerohockey

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.graphics.scale
import androidx.navigation.findNavController
import kotlin.math.abs

class GameView2(context: Context,field:Int,puck:Int,striker:Int): SurfaceView(context), Runnable {
    private var gameThread = Thread(this)
    private var running = true
    private val backgroundPaint = Paint(Color.BLUE)
    private val goalPaint = Paint()
    private val scored = Paint()
    private val currField =field
    private val currPuck =puck
    private val currStriker =striker
    private lateinit var  striker: Bitmap
    private val  striker0: Bitmap =BitmapFactory.decodeResource(context.resources, R.drawable.striker0)
    private val  striker1: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.striker1)
//    private  var  striker=striker0
    private var puck: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.puck)
    private val goal: Bitmap =BitmapFactory.decodeResource(context.resources, R.drawable.goal)
    private var locX = 0
    private var locY = 0
    private var enX = 0
    private var enY = 0
    private var vecX = 0
    private var vecY = 0
    private var score = 0
    private var ticks = 0

    private fun setupRes() {

        if(currField==0){
            backgroundPaint.color = Color.BLUE
        }else {
            backgroundPaint.color = Color.GREEN
        }
        backgroundPaint.style = Paint.Style.FILL
        if(backgroundPaint.color!= Color.RED) scored.color = Color.RED
        else scored.color = Color.BLUE
        scored.textSize = 40f
        if(backgroundPaint.color!= Color.RED) goalPaint.color = Color.RED
        else goalPaint.color = Color.BLUE
        goalPaint.style = Paint.Style.FILL
        goalPaint.textSize = 60f
        if (currStriker==0) {
            striker = striker0
        }
        if (currStriker==1) {
            striker = striker1
        }
        else striker = striker0
        striker = striker.scale(200,200)
        puck = puck.scale(100,100)
    }


    init {
        setupRes()
    }

    fun collision(dir: Boolean) {
        if (dir) {
            vecX = -vecX
        } else {
            vecY = -vecY
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        locX = event.x.toInt() - striker.width / 2
        locY = event.y.toInt() - striker.height / 2
        return false

    }

    private fun endGame(){
//        stop()
//        findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
    }

    fun stop(){
        running=false
        try {
            gameThread.join()
        } catch (e: InterruptedException){
        }
    }


    override fun run() {
        if(Settings.field==0){
            backgroundPaint.color = Color.BLUE
        }else {
            backgroundPaint.color = Color.GREEN
        }
        backgroundPaint.style = Paint.Style.FILL
        if(backgroundPaint.color!= Color.RED) scored.color = Color.RED
        else scored.color = Color.BLUE
        scored.textSize = 40f
        if(backgroundPaint.color!= Color.RED) goalPaint.color = Color.RED
        else goalPaint.color = Color.BLUE
        goalPaint.style = Paint.Style.FILL
        goalPaint.textSize = 60f
        if (Settings.striker==0) {
            striker = striker0
        }
        if (Settings.striker==1) {
            striker = striker1
        }

        else striker = striker0
        setupRes()
        var smileX = 0
        enX = 50
        enY = 50
        var smileY = 0
        vecX = 3
        vecY = 4
        var init = true
        while (running) {
            if (score>2)
                endGame()
            val canvas = holder.lockCanvas()
            if(init)
                setupRes()
//            init = false
            val text = "The current score is $score"
            if (canvas!=null) {
                try {
                    if (locY < canvas.height / 2) locY = canvas.height / 2
                    if (smileX < 0) smileX = 0
                    if (smileX + puck.width > canvas.width) smileX = canvas.width - puck.width
                    if (smileY < 0 && (smileX + puck.width < canvas.width / 3 || smileX > canvas.width * 2 / 3)) smileY = 0
                    if (smileY + puck.height > canvas.height) smileY = canvas.height - puck.height
                    canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), backgroundPaint)
                    canvas.drawRect((canvas.width / 3).toFloat(), 0f, (canvas.width * 2 / 3).toFloat(), 10f, goalPaint)
                    //                    canvas.drawBitmap(goal,canvas.getWidth()/2-goal.getWidth()/2,0,goalPaint);
                    canvas.drawBitmap(puck, smileX.toFloat(), smileY.toFloat(), backgroundPaint)
                    canvas.drawBitmap(striker, locX.toFloat(), locY.toFloat(), backgroundPaint)
                    //                    canvas.drawBitmap(enemyStriker, enX, enY, backgroundPaint);
                    canvas.drawText("The field is $currField", (canvas.width / 2).toFloat(), 150f, scored)
                    canvas.drawText("The striker is $currStriker", (canvas.width / 2).toFloat(), 180f, scored)
                    canvas.rotate(90f)
                    canvas.drawText(text, (canvas.width / 2).toFloat(), -10f, scored)
                    canvas.rotate(270f)
                    if (ticks > 0) {
                        canvas.drawText("Goal!!!", (canvas.width / 2 - 30).toFloat(), (canvas.height / 2).toFloat(), goalPaint)
                        ticks--
                    }
                    smileX += vecX
                    smileY += vecY
                    if (smileX <= 0 || smileX + puck.width >= canvas.width) collision(true)
                    if (smileY <= 0 && (smileX < canvas.width / 3 - puck.width / 2 || smileX > canvas.width * 2 / 3 - puck.width / 2)
                            || smileY + puck.height >= canvas.height) collision(false)
                    if (smileX + puck.width >= locX && smileX + puck.width <= locX + 1 + abs(vecX)
                            && smileY + puck.height > locY && smileY < locY + striker.height) collision(true)
                    if (smileX <= locX + striker.width && smileX >= locX + striker.width - 1 - abs(vecX)
                            && smileY + puck.height > locY && smileY < locY + striker.height) collision(true)
                    if (smileX > locX && smileX < locX + striker.width && smileY + puck.height >= locY && smileY < locY + abs(vecY)) collision(false)
                    if (smileX > locX && smileX < locX + striker.width && smileY <= locY + striker.height
                            && smileY > locY + striker.height - abs(vecY)) collision(false)
                    if (smileY + puck.height / 2 < 0) {
                        score += 1
                        ticks = 50
                        smileX = canvas.width / 2
                        smileY = canvas.height / 3
                    }
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    fun pause() {

    }

    fun resume() {

    }

    fun start() {
        gameThread = Thread(this)
        gameThread.start()
    }

}