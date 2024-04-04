package com.kys2024.dietcoach.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class NutritionProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    var progressWidth: Float = 80f
    private var proteinPercentage: Float = 0f
    private var carbPercentage: Float = 0f
    private var fatPercentage: Float = 0f

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 60f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    fun setNutritionValues(protein: Float, carb: Float, fat: Float) {
        val total = protein + carb + fat
        proteinPercentage = (protein / total) * 360
        carbPercentage = (carb / total) * 360
        fatPercentage = (fat / total) * 360
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = Math.min(width, height).toFloat()
        val radius = size / 2 - progressWidth

        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius)

        // 단백질
        paint.color = Color.CYAN
        canvas?.drawArc(rectF, 270f, proteinPercentage, false, paint)

        // 탄수화물
        paint.color = Color.YELLOW
        canvas?.drawArc(rectF, 270 + proteinPercentage, carbPercentage, false, paint)

        // 지방
        paint.color = Color.GREEN
        canvas?.drawArc(rectF, 270 + proteinPercentage + carbPercentage, fatPercentage, false, paint)

        val startProtein = 270f // 단백질 시작 각도
        val startCarb = startProtein + proteinPercentage
        val startFat = startCarb + carbPercentage

        // 텍스트 그리기
        drawTextOnArc(canvas, "단백질", startProtein, proteinPercentage, radius)
        drawTextOnArc(canvas, "탄수화물", startCarb, carbPercentage, radius)
        drawTextOnArc(canvas, "지방", startFat, fatPercentage, radius)
    }

    private fun drawTextOnArc(canvas: Canvas, text: String, startAngle: Float, sweepAngle: Float, radius: Float) {
        val textRadius = radius + progressWidth / 2
        val middleAngle = startAngle + sweepAngle / 2


        val xPos = width / 2 + Math.cos(Math.toRadians(middleAngle.toDouble())) * textRadius
        val yPos = height / 2 + Math.sin(Math.toRadians(middleAngle.toDouble())) * textRadius


        val textHeight = textPaint.descent() - textPaint.ascent()
        val textOffset = textHeight / 2 - textPaint.descent()

        canvas.drawText(text, xPos.toFloat(), yPos.toFloat() + textOffset, textPaint)
    }

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = progressWidth
    }
}