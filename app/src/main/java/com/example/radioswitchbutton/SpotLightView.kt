package com.example.radioswitchbutton

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SpotLightView : View {
	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	)
	
	private var lightLength = 15F
	
	private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		color = Color.parseColor("#00CC00")
		style = Paint.Style.FILL_AND_STROKE
		strokeWidth = 1f
		strokeCap = Paint.Cap.ROUND
		maskFilter = BlurMaskFilter(lightLength, BlurMaskFilter.Blur.SOLID)
	}
	
	init {
		minimumHeight = (3 * lightLength).toInt()
	}
	
	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		canvas?.drawRoundRect(
			lightLength,
			lightLength,
			right.toFloat() - lightLength,
			bottom.toFloat() - lightLength,
			lightLength,
			lightLength,
			paint
		)
	}
	
	fun setLightLength(length: Float) {
		this.lightLength = length
	}
	
	//hint:about 3 * spotLightViewHeight = 45
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, (3 * lightLength).toInt())
		
	}
}