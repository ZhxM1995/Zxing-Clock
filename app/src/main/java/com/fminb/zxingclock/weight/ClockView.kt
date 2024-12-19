package com.fminb.zxingclock.weight

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.icu.util.Calendar
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnStart
import kotlin.math.cos
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 30f
        color = Color.BLACK
    }

    private val centerPointPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#603ED7")
    }

    private var animator: ValueAnimator? = null

    init {
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000L
            repeatCount = ValueAnimator.INFINITE
            doOnStart {
                invalidate() // 强制立即更新视图
            }
            addUpdateListener {
                invalidate()
            }
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = minOf(width, height)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(centerX, centerY) * 0.75f

        drawClockFace(canvas, centerX, centerY, radius)
        drawHands(canvas, centerX, centerY, radius)
        drawCenterPoint(canvas, centerX, centerY)
    }

    private fun drawClockFace(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        paint.color = Color.parseColor("#5C4FCC")
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    private fun drawHands(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val millisecond = calendar.get(Calendar.MILLISECOND)

        // Calculate the exact position of the second hand
        val secondAngle = (second * 6 + millisecond / 1000.0 * 6) % 360

        // Draw the hour hand
        paint.strokeWidth = 15f
        paint.color = Color.parseColor("#8670D4")
        val hourAngle = ((hour * 30 + minute / 60.0 * 30) % 360).toFloat()
        drawHand(canvas, centerX, centerY, radius * 0.43f, hourAngle)

        // Draw the minute hand
        paint.strokeWidth = 12f
        paint.color = Color.parseColor("#8670D4")
        val minuteAngle = ((minute * 6 + second / 60.0 * 6) % 360).toFloat()
        drawHand(canvas, centerX, centerY, radius * 0.72f, minuteAngle)

        // Draw the second hand
        paint.strokeWidth = 3f
        paint.color = Color.parseColor("#CBBDFD")
        drawHand(canvas, centerX, centerY, radius * 0.85f, secondAngle.toFloat())
    }

    private fun drawCenterPoint(canvas: Canvas, centerX: Float, centerY: Float) {
        canvas.drawCircle(centerX, centerY, 22.5f, centerPointPaint)
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, length: Float, angle: Float) {
        val radian = Math.toRadians((angle - 90).toDouble())
        val endX = cx + length * cos(radian)
        val endY = cy + length * sin(radian)
        canvas.drawLine(cx, cy, endX.toFloat(), endY.toFloat(), paint)
    }
}
