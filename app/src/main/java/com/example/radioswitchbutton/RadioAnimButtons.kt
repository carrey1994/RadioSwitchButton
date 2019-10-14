package com.example.radioswitchbutton

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import kotlin.math.abs


class RadioAnimButtons : FrameLayout {
	
	private var isStart = false
	private var lastClickViewId = 1
	private val titles = arrayOf("支出", "收入", "借債")
	private var numbers = titles.size
	private val buttons = arrayListOf<Button>()
	private val gapPadding = 10
	
	private var listener: OnSwitchListener? = null
	
	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	)
	
	init {
		setPadding(0, 0, 0, 8)
		
		//prepare buttonsLayout for add buttons
		val buttonsLayout = LinearLayout(context).apply {
			val params = LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
			)
			params.setMargins(0, 0, 0, 5)
			layoutParams = params
			weightSum = numbers.toFloat()
			orientation = LinearLayout.HORIZONTAL
		}
		this.addView(buttonsLayout)
		
		//add buttons
		for (i in 0 until numbers) {
			Button(context).apply {
				val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
				this.id = i + 1//TODO::id must not be Zero!!
				params.weight = 1f
				params.setMargins(gapPadding, 0, gapPadding, 0)
				
				buttonsLayout.addView(this, params)
				setBackgroundColor(Color.TRANSPARENT)
				text = titles[i]
				stateListAnimator = null
				setTextColor(Color.WHITE)
				gravity = Gravity.CENTER
				buttons.add(this)
			}
		}
		
		
		//prepare indicator View
		val viewLayout = LinearLayout(context).apply {
			val params = LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
			)
			params.gravity = Gravity.BOTTOM
			layoutParams = params
			weightSum = numbers.toFloat()
			orientation = LinearLayout.HORIZONTAL
		}
		this.addView(viewLayout)
		val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
		params.weight = 1f
		val view = SpotLightView(context).apply {
			layoutParams = params
		}
		viewLayout.addView(view, params)
		
		//setButtonsOnClickEvent and Animation
		for (button in buttons) {
			button.setOnClickListener { clickButton ->
				val lastViewId = lastClickViewId
				if (isStart.not())
					when (lastClickViewId) {
						//shrink and recover at original position
						clickButton.id -> {
							isStart = true
							view.pivotX = 100f
							val shrinkX = ObjectAnimator.ofFloat(view, "ScaleX", 1f, 0.8f).apply {
								duration = 500
							}
							
							val shrinkY = ObjectAnimator.ofFloat(view, "ScaleY", 1f, 0.5f).apply {
								duration = 500
							}
							val aniSetShrink = AnimatorSet()
							aniSetShrink.playTogether(shrinkX, shrinkY)
							aniSetShrink.start()
							Handler().postDelayed({
								
								val scaleX2 = ObjectAnimator.ofFloat(view, "ScaleX", 0.8f, 1f).apply {
									duration = 500
								}
								
								val scaleY2 = ObjectAnimator.ofFloat(view, "ScaleY", 0.5f, 1f).apply {
									duration = 500
								}
								
								val aniSetRecover = AnimatorSet()
								aniSetRecover.doOnEnd { isStart = false }
								aniSetRecover.playTogether(scaleX2, scaleY2)
								aniSetRecover.start()
							}, 500)
						}
						//shrink, recover and translate at clicked position
						else -> {
							isStart = true
							lastClickViewId = clickButton.id
							view.pivotX = 0f
							
							if (lastClickViewId < lastViewId)
								view.pivotX = view.width.toFloat()
							
							val scale = 1 + abs(lastClickViewId - lastViewId).toFloat()
							
							val enlargeX = ObjectAnimator.ofFloat(view, "ScaleX", 1f, scale).apply {
								duration = 500
							}
							
							val shrinkY = ObjectAnimator.ofFloat(view, "ScaleY", 1f, 0.5f).apply {
								duration = 500
							}
							
							val aniSetSizeChange = AnimatorSet()
							aniSetSizeChange.playTogether(enlargeX, shrinkY)
							aniSetSizeChange.start()
							
							Handler().postDelayed({
								
								val shrinkX = ObjectAnimator.ofFloat(view, "ScaleX", scale, 1f).apply {
									duration = 500
								}
								
								val enlargeY = ObjectAnimator.ofFloat(view, "ScaleY", 0.5f, 1f).apply {
									duration = 500
								}
								
								val translate = ObjectAnimator.ofFloat(
									view,
									"TranslationX",
									((buttons.indexOf(clickButton)) * view.width.toFloat())
								).apply {
									duration = 500
								}
								
								val aniSetRecoverTrans = AnimatorSet()
								aniSetRecoverTrans.playTogether(shrinkX, enlargeY, translate)
								aniSetRecoverTrans.doOnEnd {
									isStart = false
									listener?.onSwitch(buttons.indexOf(clickButton))
								}
								aniSetRecoverTrans.start()
								
							}, 500)
						}
						
					}
			}
		}
		
	}
	
	
	interface OnSwitchListener {
		fun onSwitch(pos: Int)
	}
	
	fun setOnSwitchListener(listener: OnSwitchListener) {
		this.listener = listener
	}
	
}