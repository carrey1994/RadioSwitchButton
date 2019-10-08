package com.example.radioswitchbutton

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout


class RadioAnimButtons : RelativeLayout {

    private var afterPos = 0f
    var isStart = false
    var lastViewId = 0
    private var numbers = 3
    private val buttons = arrayListOf<Button>()
    private val gapPadding = 10
    private val titles = arrayOf("支出", "收入", "借債")

    private var listener: OnSwitchListener? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {

        for (i in 0 until numbers) {
            Button(context).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                this.id = i + 1//TODO::id must not be Zero!!
                if (i != 0) {
                    params.addRule(RIGHT_OF, buttons[i - 1].id)
                } else {
                    params.addRule(ALIGN_PARENT_LEFT, TRUE)
                    params.addRule(ALIGN_PARENT_TOP, TRUE)
                }
                params.setMargins(gapPadding, 0, gapPadding, 0)

                addView(this, params)
                background = context.getDrawable(R.drawable.shape_radius_false_button)
                text = titles[i]
                stateListAnimator = null
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                buttons.add(this)
            }
        }


        val view = FrameLayout(context)
        view.background = context.getDrawable(R.drawable.shape_radio_button)
        val params = LayoutParams(0, 0)
        params.setMargins(gapPadding, 0, gapPadding, 0)
        params.addRule(ALIGN_PARENT_START, TRUE)
        params.addRule(ALIGN_PARENT_TOP, TRUE)
        params.addRule(LEFT_OF, buttons[1].id)
        params.addRule(ALIGN_BOTTOM, buttons[1].id)

        addView(view, params)

        for (button in buttons) {
            button.setOnClickListener {
                if (isStart.not() && lastViewId != it.id) {
                    val distant = button.x - view.x
                    lastViewId = it.id

                    val animation = TranslateAnimation(afterPos, distant, 0f, 0f).apply {
                        this.duration = 500
                        fillAfter = true
                        isFillEnabled = true
                        setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationRepeat(p0: Animation?) {
                            }

                            override fun onAnimationEnd(p0: Animation?) {
                                buttons.forEach { button -> button.isSelected = false }
                                isStart = false
                                it.isSelected = true
                            }

                            override fun onAnimationStart(p0: Animation?) {
                                isStart = true
                                listener?.onSwitch(buttons.indexOf(it))
                            }
                        })
                    }
                    afterPos = button.x
                    view.startAnimation(animation)
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