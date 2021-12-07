package com.example.aerohockey

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.graphics.scale
import kotlinx.coroutines.delay
import kotlin.math.abs

class GameView2(context: Context, field: Int, puck: Int, striker: Int): SurfaceView(context), Runnable {
    private var gameThread = Thread(this)
    private var running = true
    private val backgroundPaint = Paint(Color.BLUE)
    private val goalPaint = Paint()
    private val outlinePaint = Paint()
    private val scored = Paint()
    private val currField =field
    private val currPuck =puck
    private val currStriker =striker
    private lateinit var  striker: Bitmap
    private val  striker0: Bitmap =BitmapFactory.decodeResource(context.resources, R.drawable.striker0)
    private val  striker1: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.striker1)
//
    private var enemyStriker:Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.striker0)

    private var puck: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.puck)
    private val goal: Bitmap =BitmapFactory.decodeResource(context.resources, R.drawable.goal)
    private var strikerX = 0
    private var strikerY = 0
    private var enemyX = 0
    private var enemyY = 0
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
        outlinePaint.color=Color.RED
        outlinePaint.style=Paint.Style.STROKE
        if (currStriker==0) {
            striker = striker0
        }
        if (currStriker==1) {
            striker = striker1
        }
        else striker = striker0
        striker = striker.scale(200, 200)
        puck = puck.scale(100, 100)
        enemyStriker = enemyStriker.scale(200,200)
    }


    init {
        setupRes()
    }

    private fun collisionHandling(dir: Boolean) {
        if (dir) {
            vecX = -vecX
        } else {
            vecY = -vecY
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        if (event.y.toInt()>holder.lockCanvas().height/2) {
            strikerX = event.x.toInt() - striker.width / 2
            strikerY = event.y.toInt() - striker.height / 2
//        }
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
//        if(Settings.field==0){
//            backgroundPaint.color = Color.BLUE
//        }else {
//            backgroundPaint.color = Color.GREEN
//        }
//        backgroundPaint.style = Paint.Style.FILL
//        if(backgroundPaint.color!= Color.RED) scored.color = Color.RED
//        else scored.color = Color.BLUE
//        scored.textSize = 40f
//        if(backgroundPaint.color!= Color.RED) goalPaint.color = Color.RED
//        else goalPaint.color = Color.BLUE
//        goalPaint.style = Paint.Style.FILL
//        goalPaint.textSize = 60f
//        if (Settings.striker==0) {
//            striker = striker0
//        }
//        if (Settings.striker==1) {
//            striker = striker1
//        }
//        else striker = striker0
        setupRes()
        var puckX = 0
        enemyX = 400
        enemyY = 0
        var puckY = 0
        var delay = 0
        vecX = 3
        vecY = 4
        val speed = 30
        var init = true
        var enemyChasing = true
        while (running) {
            if (score>2)
                endGame()
            val canvas = holder.lockCanvas()
            if(init)
                setupRes()
            init = false
            val text = "The current score is $score"
            if (canvas!=null) {
                try {
                    //Bounds handling
                    if (strikerY < canvas.height / 2) strikerY = canvas.height / 2
                    if (puckX < 0) puckX = 0
                    if (puckX + puck.width > canvas.width) puckX = canvas.width - puck.width
                    if (puckY < 0 && (puckX + puck.width < canvas.width / 3 || puckX > canvas.width * 2 / 3)) puckY = 0
                    if (puckY + puck.height > canvas.height) puckY = canvas.height - puck.height
                    //Draw stuff
                    canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), backgroundPaint)
                    canvas.drawRect((canvas.width / 3).toFloat(), 0f, (canvas.width * 2 / 3).toFloat(), 10f, goalPaint)
                    //                    canvas.drawBitmap(goal,canvas.getWidth()/2-goal.getWidth()/2,0,goalPaint);
                    canvas.drawBitmap(puck, puckX.toFloat(), puckY.toFloat(), backgroundPaint)
                    //Outlines
                    canvas.drawRect(puckX.toFloat(), puckY.toFloat(), (puckX+puck.width).toFloat(), (puckY+puck.height).toFloat(),outlinePaint)
                    canvas.drawRect(strikerX.toFloat(), strikerY.toFloat(), (strikerX+striker.width).toFloat(), (strikerY+striker.height).toFloat(),outlinePaint)
                    canvas.drawRect(enemyX.toFloat(), enemyY.toFloat(), (enemyX+enemyStriker.width).toFloat(), (enemyY+enemyStriker.height).toFloat(),outlinePaint)
                    canvas.drawRect(enemyX.toFloat(), (enemyY + enemyStriker.height - 2*(abs(vecY)+1)).toFloat(), (enemyX+enemyStriker.width).toFloat(), (enemyY+enemyStriker.height).toFloat(),outlinePaint)

                    canvas.drawBitmap(striker, strikerX.toFloat(), strikerY.toFloat(), backgroundPaint)
                    canvas.drawBitmap(enemyStriker, enemyX.toFloat(), enemyY.toFloat(), backgroundPaint);
                    canvas.drawText("The field is $currField", (canvas.width / 2).toFloat(), 150f, scored)
                    canvas.drawText("The striker is $currStriker", (canvas.width / 2).toFloat(), 180f, scored)
                    canvas.rotate(90f)
                    canvas.drawText(text, (canvas.width / 2).toFloat(), -10f, scored)
                    canvas.rotate(270f)
                    enemyChasing = puckY+enemyStriker.height/2<canvas.height/2
                    //Move enemy
                    if (enemyChasing) {
                        if (delay>0){
                            enemyX+=(canvas.width/2-enemyStriker.width/2-enemyX)/speed
                            enemyY+=(50-enemyY)/speed
                            delay--
                        }
                        else {
                            if (enemyX + puck.width / 2 < puckX) enemyX += abs(vecX)
                            if (enemyX + puck.width / 2 > puckX) enemyX -= abs(vecX)
                            if (enemyY + puck.height / 2 < puckY) enemyY += abs(vecY)
                            if (enemyY + puck.height / 2 > puckY) enemyY -= abs(vecY)
                        }
                    }
                    else{
                        enemyX+=(canvas.width/2-enemyStriker.width/2-enemyX)/speed
                        enemyY+=(50-enemyY)/speed
                    }

                    //Draw goal message
                    if (ticks > 0) {
                        canvas.drawText("Goal!!!", (canvas.width / 2 - 30).toFloat(), (canvas.height / 2).toFloat(), goalPaint)
                        ticks--
                    }
                    //Move puck
                    puckX += vecX
                    puckY += vecY
                    //Collision
                    //Horizontal collision
                    if (puckX <= 0 || puckX + puck.width >= canvas.width) collisionHandling(true)
                    //Vertical collision
                    if ((puckY <= 0 && (puckX < canvas.width / 3 - puck.width / 2 || puckX > canvas.width * 2 / 3 - puck.width / 2) )
                            || puckY + puck.height >= canvas.height) collisionHandling(false)
                    //Collision with striker
                    //Horizontal
                    if (puckX + puck.width >= strikerX && puckX + puck.width <= strikerX + 1 + abs(vecX)
                            && puckY + puck.height > strikerY && puckY < strikerY + striker.height) collisionHandling(true)

                    if (puckX <= strikerX + striker.width && puckX >= strikerX + striker.width - 1 - abs(vecX)
                            && puckY + puck.height > strikerY && puckY < strikerY + striker.height) collisionHandling(true)
                    //Vertical
                    if (puckX > strikerX && puckX < strikerX + striker.width && puckY + puck.height >= strikerY && puckY < strikerY + abs(vecY)) collisionHandling(false)

                    if (puckX > strikerX && puckX < strikerX + striker.width && puckY <= strikerY + striker.height
                            && puckY > strikerY + striker.height - abs(vecY)) collisionHandling(false)
                    //Collision with enemy striker
                    //Horizontal
                    //left
                    if (RectF(puckX.toFloat(), puckY.toFloat(), (puckX+puck.width).toFloat(), (puckY+puck.height).toFloat()).intersect(
                                    RectF(enemyX.toFloat(), (enemyY).toFloat(), (enemyX+ abs(vecX)).toFloat(), (enemyY+ enemyStriker.height).toFloat()
                                    ))){
                        puckX=enemyX-puck.width- abs(vecX)
                        collisionHandling(true)
                        enemyChasing=false
                        delay=10
                    }
                    //right
                    if (RectF(puckX.toFloat(), puckY.toFloat(), (puckX+puck.width).toFloat(), (puckY+puck.height).toFloat()).intersect(
                                    RectF((enemyX+enemyStriker.width- abs(vecX)).toFloat(), (enemyY).toFloat(), (enemyX+ enemyStriker.width).toFloat(), (enemyY+ enemyStriker.height).toFloat()
                                    ))){
                        puckX=enemyX+enemyStriker.width- abs(vecX)
                        collisionHandling(true)
                        enemyChasing=false
                        delay=10
                    }
                    //Vertical
                    //bottom
                    if (RectF(puckX.toFloat(), puckY.toFloat(), (puckX+puck.width).toFloat(), (puckY+puck.height).toFloat()).intersect(
                                    RectF((enemyX+ abs(vecX)).toFloat(), (enemyY+enemyStriker.height- abs(vecY)).toFloat(), (enemyX+enemyStriker.width-abs(vecX)).toFloat(), (enemyY+enemyStriker.height).toFloat()
                    ))){
                        puckY=enemyY+enemyStriker.height+ abs(vecY)
                        collisionHandling(false)
                        enemyChasing=false
                        delay=10
                    }
                    //top
                    if (RectF(puckX.toFloat(), puckY.toFloat(), (puckX+puck.width).toFloat(), (puckY+puck.height).toFloat()).intersect(
                                    RectF(enemyX.toFloat(), (enemyY).toFloat(), (enemyX+enemyStriker.width).toFloat(), (enemyY+ abs(vecY)).toFloat()
                                    ))){
                        puckY=enemyY- abs(vecY)-puck.height
                        collisionHandling(false)
                        enemyChasing=false
                        delay=10
                    }
                    //Goal handling
                    if (puckY + puck.height / 2 < 0) {
                        score += 1
                        ticks = 50
                        puckX = canvas.width / 2
                        puckY = canvas.height / 3
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