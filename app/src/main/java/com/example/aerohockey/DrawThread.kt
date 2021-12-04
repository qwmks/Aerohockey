package com.example.aerohockey

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import androidx.core.graphics.scale
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController

class DrawThread(context: Context, surfaceHolder: SurfaceHolder, private val fieldN: Int, strikerN:Int, puckN:Int) :
    Thread() {
    private val surfaceHolder: SurfaceHolder
    @Volatile
    private var running = true //флаг для остановки потока
    private val backgroundPaint = Paint()
    private val goalPaint = Paint()
    private val scored = Paint()
    private lateinit var  striker: Bitmap
    private lateinit var  striker0: Bitmap
    private lateinit var  striker1: Bitmap
    private lateinit var  strikerEnemy: Bitmap
//    private val enemyStriker: Bitmap
    private val puck: Bitmap
    private val goal: Bitmap
    private val towardPointX = 0
    private val towardPointY = 0
    private var locX = 0
    private var locY = 0
    private val contextIn:Context = context
    private var enX = 0
    private var enY = 0
    private var vecX = 0
    private var vecY = 0
    private var score = 0
    private var ticks = 0
    fun requestStop() {
        running = false
    }
    fun requestStart(){
        running = true
    }
    fun setStrikerPoint(x: Int, y: Int) {
        locX = x - striker.width / 2
        locY = y - striker.height / 2
        //        towardPointX = x+puck.getWidth()/2-bitmap.getWidth()/2;
//        towardPointY = y-puck.getHeight()/2;
    }
    fun collision(dir: Boolean) {
        if (dir) {
            vecX = -vecX
        } else {
            vecY = -vecY
        }
    }

    override fun run() {
        var smileX = 0
        enX = 50
        enY = 50
        var smileY = 0
        val delay = 50
        //        Random rand = new Random();
//        vecX = rand.nextInt((20 - -20) + 1) + -20;
//        vecY = rand.nextInt((20 - 0) + 1) + 0;
        vecX = 3
        vecY = 4
        var init = true
        while (running) {
            val canvas = surfaceHolder.lockCanvas()
            if(init) setupRes()
//            init = false
            val text = "The current score is $score"
            var field = "The field is $fieldN"
            if (canvas != null) {
                try {
                    if (locY < canvas.height / 2) locY = canvas.height / 2
                    if (smileX < 0) smileX = 0
                    if (smileX + puck.width > canvas.width) smileX = canvas.width - puck.width
                    if (smileY < 0 && (smileX + puck.width < canvas.width / 3 || smileX > canvas.width * 2 / 3)) smileY =
                        0
                    if (smileY + puck.height > canvas.height) smileY = canvas.height - puck.height
                    canvas.drawRect(
                        0f,
                        0f,
                        canvas.width.toFloat(),
                        canvas.height.toFloat(),
                        backgroundPaint
                    )
                    canvas.drawRect(
                        (canvas.width / 3).toFloat(),
                        0f,
                        (canvas.width * 2 / 3).toFloat(),
                        10f,
                        goalPaint
                    )
                    //                    canvas.drawBitmap(goal,canvas.getWidth()/2-goal.getWidth()/2,0,goalPaint);
                    canvas.drawBitmap(puck, smileX.toFloat(), smileY.toFloat(), backgroundPaint)
                    canvas.drawBitmap(striker, locX.toFloat(), locY.toFloat(), backgroundPaint)
                    //                    canvas.drawBitmap(enemyStriker, enX, enY, backgroundPaint);
                    canvas.drawText(field, (canvas.width / 2).toFloat(), 100f, scored)
                    canvas.drawText("The field is ${Settings.field}", (canvas.width / 2).toFloat(), 150f, scored)
                    canvas.rotate(90f)
                    canvas.drawText(text, (canvas.width / 2).toFloat(), -10f, scored)

                    canvas.rotate(270f)
                    if (ticks > 0) {
                        canvas.drawText(
                            "Goal!!!",
                            (canvas.width / 2 - 30).toFloat(),
                            (canvas.height / 2).toFloat(),
                            goalPaint
                        )
                        ticks--
                    }
                    smileX += vecX
                    smileY += vecY
//                                        if (delay>0){
//                        delay--;
//                    }else {
//                        if (enX + enemyStriker.getWidth() / 2 < smileX) enX += 3;
//                        if (enX + enemyStriker.getWidth() / 2 > smileX) enX -= 3;
//                        if (enY + enemyStriker.getHeight() / 2 < smileY) enY += 4;
//                        if (enY + enemyStriker.getHeight() / 2 > smileY) enY -= 4;
//
//                        if ((smileX + puck.getWidth() >= locX) && (smileX + puck.getWidth() <= locX + 1 + abs(vecX)) && (smileY + puck.getHeight() > locY) && (smileY < locY + enemyStriker.getHeight()))
//                            collision(true);
//                        if ((smileX <= locX + enemyStriker.getWidth()) && (smileX >= locX + enemyStriker.getWidth() - 1 - abs(vecX)) && (smileY + puck.getHeight() > locY) && (smileY < locY + enemyStriker.getHeight()))
//                            collision(true);
//                        if ((smileX > locX) && (smileX < locX + enemyStriker.getWidth()) && (smileY + puck.getHeight() >= locY) && (smileY < locY + abs(vecY)))
//                            collision(false);
//                        if ((smileX > locX) && (smileX < locX + enemyStriker.getWidth()) && (smileY <= locY + enemyStriker.getHeight()) && (smileY > locY + enemyStriker.getHeight() - abs(vecY)))
//                            collision(false);
//                    }//Enemy striker
                    if (smileX <= 0 || smileX + puck.width >= canvas.width) collision(true)
                    if (smileY <= 0 && (smileX < canvas.width / 3 - puck.width / 2 || smileX > canvas.width * 2 / 3 - puck.width / 2) || smileY + puck.height >= canvas.height) collision(
                        false
                    )
                    if (smileX + puck.width >= locX && smileX + puck.width <= locX + 1 + Math.abs(
                            vecX
                        ) && smileY + puck.height > locY && smileY < locY + striker.height
                    ) collision(true)
                    if (smileX <= locX + striker.width && smileX >= locX + striker.width - 1 - Math.abs(
                            vecX
                        ) && smileY + puck.height > locY && smileY < locY + striker.height
                    ) collision(true)
                    if (smileX > locX && smileX < locX + striker.width && smileY + puck.height >= locY && smileY < locY + Math.abs(
                            vecY
                        )
                    ) collision(false)
                    if (smileX > locX && smileX < locX + striker.width && smileY <= locY + striker.height && smileY > locY + striker.height - Math.abs(
                            vecY
                        )
                    ) collision(false)
                    if (smileY + puck.height / 2 < 0) {
                        score += 1
                        ticks = 50
                        smileX = canvas.width / 2
                        smileY = canvas.height / 3

//
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    private fun setupRes() {
        if (Settings.striker==0) {
            striker = striker0
        }
        if (Settings.striker==1) {
            striker = striker1
        }
        striker.scale(200,200)
    }

    fun getScore():Int {
        return score
    }

    init {
        puck = BitmapFactory.decodeResource(context.resources, R.drawable.smile)
        striker0 = BitmapFactory.decodeResource(contextIn.resources, R.drawable.striker0)
        striker1 = BitmapFactory.decodeResource(contextIn.resources, R.drawable.striker1)
//        enemyStriker = BitmapFactory.decodeResource(context.resources, R.drawable.striker)
        goal = BitmapFactory.decodeResource(context.resources, R.drawable.goal)
        this.surfaceHolder = surfaceHolder
        if(fieldN==0){
            backgroundPaint.color = Color.BLUE
        }else {
            backgroundPaint.color = Color.GREEN
        }
        backgroundPaint.style = Paint.Style.FILL
        if(backgroundPaint.color!=Color.RED) scored.color = Color.RED
        else scored.color = Color.BLUE
        scored.textSize = 40f
        if(backgroundPaint.color!=Color.RED) goalPaint.color = Color.RED
        else goalPaint.color = Color.BLUE
        goalPaint.style = Paint.Style.FILL
        goalPaint.textSize = 60f
    }
}