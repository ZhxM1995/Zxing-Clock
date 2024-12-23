package com.fminb.zxingclock.weight

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.icu.util.Calendar
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnStart
import com.fminb.zxingclock.R
import com.fminb.zxingclock.utils.Utils
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
        strokeCap = Paint.Cap.ROUND
    }

    private val backgroundBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.bg)
    private lateinit var scaledBackgroundBitmap: Bitmap

    private var length: Float = Utils.dp2px(80f)

    private var animator: ValueAnimator? = null

    private var resetPaintAfterDraw: (() -> Unit)? = null

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
        length = size * 0.2f
        val newSize = (size * 0.93).toInt()
        // 如果还没有缩放背景图片，则进行缩放
        if (!::scaledBackgroundBitmap.isInitialized || scaledBackgroundBitmap.width != newSize) {
            try {
                scaledBackgroundBitmap =
                    Bitmap.createScaledBitmap(backgroundBitmap, newSize, newSize, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(centerX, centerY) * 0.75f

        drawClockFace(canvas, centerX, centerY)
        drawCrossHair(canvas, centerX, centerY)
        drawHands(canvas, centerX, centerY, radius)
        drawCenterPoint(canvas, centerX, centerY)
    }

    private fun drawCrossHair(canvas: Canvas, centerX: Float, centerY: Float) {
        setCrossHairPaint()

        // 绘制垂直线
        canvas.drawLine(
            centerX,
            centerY - length / 2,
            centerX,
            centerY + length / 2,
            paint
        )

        // 绘制水平线
        canvas.drawLine(
            centerX - length / 2,
            centerY,
            centerX + length / 2,
            centerY,
            paint
        )

        resetPaintAfterDraw?.invoke()
    }

    private fun setCrossHairPaint() {
        paint.strokeWidth = Utils.dp2px(1f)
        paint.color = Color.parseColor("#603ED7")
        paint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        resetPaintAfterDraw = { paint.pathEffect = null }
    }

    private fun drawClockFace(canvas: Canvas, centerX: Float, centerY: Float) {
        setClockFacePaint()
        // 绘制背景图片
        val bitmapLeft = centerX - scaledBackgroundBitmap.width / 2
        val bitmapTop = centerY - scaledBackgroundBitmap.height / 2
        canvas.drawBitmap(scaledBackgroundBitmap, bitmapLeft, bitmapTop, paint)
        resetPaintAfterDraw?.invoke()
    }

    private fun setClockFacePaint() {
        resetPaintAfterDraw = null
    }

    private fun drawHands(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val millisecond = calendar.get(Calendar.MILLISECOND)

        // Draw the hour
        drawHourHand(canvas, centerX, centerY, radius, hour, minute)

        // Draw the minute
        drawMinuteHand(canvas, centerX, centerY, radius, minute, second)

        // Draw the second
        drawSecondHand(canvas, centerX, centerY, radius, second, millisecond)
    }

    private fun drawHourHand(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        hour: Int,
        minute: Int
    ) {
        setHourHandPaint()
        val hourAngle = ((hour * 30 + minute / 60.0 * 30) % 360).toFloat()
        drawHand(canvas, centerX, centerY, radius * 0.43f, hourAngle)
        resetPaintAfterDraw?.invoke()
    }

    private fun setHourHandPaint() {
        paint.strokeWidth = Utils.dp2px(5f)
        paint.color = Color.parseColor("#8670D4")
        resetPaintAfterDraw = null
    }

    private fun drawMinuteHand(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        minute: Int,
        second: Int
    ) {
        setMinuteHandPaint()
        val minuteAngle = ((minute * 6 + second / 60.0 * 6) % 360).toFloat()
        drawHand(canvas, centerX, centerY, radius * 0.72f, minuteAngle)
        resetPaintAfterDraw?.invoke()
    }

    private fun setMinuteHandPaint() {
        paint.strokeWidth = Utils.dp2px(4f)
        paint.color = Color.parseColor("#8670D4")
        resetPaintAfterDraw = null
    }

    private fun drawSecondHand(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        second: Int,
        millisecond: Int
    ) {
        // Calculate the exact position of the second hand
        val secondAngle = (second * 6 + millisecond / 1000.0 * 6) % 360
        setSecondHandPaint(secondAngle)
        drawHand(canvas, centerX, centerY, radius * 0.85f, secondAngle.toFloat())
        resetPaintAfterDraw?.invoke()
    }

    private fun setSecondHandPaint(secondAngle: Double) {
        // Calculate the shadow angle (10 degrees behind the second hand)
        val shadowAngle = (secondAngle + 10) % 360
        // Convert shadow angle to radians
        val shadowRadian = Math.toRadians((shadowAngle - 180))
        // Calculate shadow offset
        val shadowOffsetX = (6f * cos(shadowRadian)).toFloat()
        val shadowOffsetY = (6f * sin(shadowRadian)).toFloat()
        // 设置画笔属性
        paint.strokeWidth = Utils.dp2px(1f)
        paint.color = Color.parseColor("#CBBDFD")
        // Set shadow layer
        paint.setShadowLayer(
            5f,
            shadowOffsetX,
            shadowOffsetY,
            Color.parseColor("#1F0E5C3D")
        )
        resetPaintAfterDraw = { paint.clearShadowLayer() }
    }

    private fun drawCenterPoint(canvas: Canvas, centerX: Float, centerY: Float) {
        setCenterPointPaint()
        canvas.drawCircle(centerX, centerY, Utils.dp2px(7.5f), paint)
        resetPaintAfterDraw?.invoke()
    }

    private fun setCenterPointPaint() {
        // 设置画笔属性
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f
        paint.color = Color.parseColor("#603ED7")
        resetPaintAfterDraw = { paint.style = Paint.Style.STROKE }
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, length: Float, angle: Float) {
        val radian = Math.toRadians((angle - 90).toDouble())
        val endX = cx + length * cos(radian)
        val endY = cy + length * sin(radian)
        canvas.drawLine(cx, cy, endX.toFloat(), endY.toFloat(), paint)
    }
}
