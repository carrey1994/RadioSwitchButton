package com.example.radioswitchbutton

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
import android.widget.RelativeLayout
import kotlin.math.abs


class RadioAnimButtons : RelativeLayout {

    private var afterPos = 0f
    var isStart = false
    var lastClickViewId = 1
    private var numbers = 4
    private val buttons = arrayListOf<Button>()
    private val gapPadding = 10
    private val titles = arrayOf("支出", "收入", "借債", "借債")

    private lateinit var listener: OnSwitchListener


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
                setBackgroundColor(Color.TRANSPARENT)
                text = titles[i]
                stateListAnimator = null
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                buttons.add(this)
            }
        }


        val view = FrameLayout(context)
        view.background = context.getDrawable(R.drawable.shape_radio_button)
        val params = LayoutParams(0, 30)
        params.setMargins(gapPadding, 0, gapPadding, 0)
        params.addRule(ALIGN_PARENT_START, TRUE)
        params.addRule(LEFT_OF, buttons[1].id)
        params.addRule(ALIGN_BOTTOM, buttons[1].id)


        addView(view, params)

        for (button in buttons) {
            button.setOnClickListener {
                val lastViewId = lastClickViewId
                buttons.indexOfFirst { it.id == lastClickViewId }
                if (isStart.not() && lastClickViewId != it.id) {
                    lastClickViewId = it.id
                    view.pivotX = 0f


                    if (lastClickViewId < lastViewId)
                        view.pivotX = view.width.toFloat()


                    val scaleX = ObjectAnimator.ofFloat(
                        view,
                        "ScaleX",
                        1f,
                        1 + abs(lastClickViewId - lastViewId).toFloat()
                    ).apply {
                        duration = 500
                    }

                    val scaleY = ObjectAnimator.ofFloat(view, "ScaleY", 1f, 0.5f).apply {
                        duration = 500
                    }


                    val aniSet = AnimatorSet()
                    aniSet.playTogether(scaleX, scaleY)
                    aniSet.start()
                    Handler().postDelayed({

                        val scaleX2 =
                            ObjectAnimator.ofFloat(
                                view,
                                "ScaleX",
                                1 + abs(lastClickViewId - lastViewId).toFloat(),
                                1f
                            ).apply {
                                duration = 500
                            }

                        val scaleY2 = ObjectAnimator.ofFloat(view, "ScaleY", 0.5f, 1f).apply {
                            duration = 500
                        }


                        val translate = ObjectAnimator.ofFloat(
                            view,
                            "TranslationX",
                            (buttons.indexOf(it) * 2f * gapPadding + (buttons.indexOf(it)) * view.width.toFloat())
                        ).apply {
                            duration = 500
                        }

                        val aniSet2 = AnimatorSet()
                        aniSet2.playTogether(scaleX2, scaleY2, translate)
                        aniSet2.start()

                    }, 500)

                    afterPos = button.x
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