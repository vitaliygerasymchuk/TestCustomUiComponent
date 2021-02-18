package app.custom.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.ViewTreeObserver.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import app.custom.component.shared.Loggable

enum class LeafViewState {
    Expanded,
    Collapsed
}


class LeafView : FrameLayout, Loggable {


    private var initialHeight: Int = 0
    private lateinit var stableDrawable: Drawable
    private lateinit var expandedDrawable: Drawable
    private var valueAnimator: ValueAnimator? = null
    private var currentState: LeafViewState = LeafViewState.Collapsed
    private var textPaint = Paint()

    var expandedColor: String = "#ffffff"
        set(value) {
            field = if (!value.contains("#")) {
                "#${value}"
            } else {
                value
            }
            DrawableCompat.setTint(expandedDrawable, Color.parseColor(field))
        }

    var title: String = if (BuildConfig.DEBUG) "Energy" else ""
        set(value) {
            field = value
            if (initialHeight != 0) {
                invalidate()
            }
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTitle(canvas)
    }

    fun expand(function: (state: LeafViewState, expandedColor: String) -> Unit) {
        setState(LeafViewState.Expanded, function)
    }

    fun collapse(function: (state: LeafViewState, expandedColor: String) -> Unit) {
        setState(LeafViewState.Collapsed, function)
    }

    private fun init() {
        setWillNotDraw(false)
        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
        textPaint.textSize = TITLE_TEXT_SIZE

        stableDrawable = ContextCompat.getDrawable(context, R.drawable.ic_leaf)!!
        expandedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_leaf)!!.mutate()

        background = stableDrawable
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (initialHeight == 0) {
                    initialHeight = height
                }
                if (initialHeight != 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    private fun setState(
        state: LeafViewState,
        function: (state: LeafViewState, expandedColor: String) -> Unit
    ) {
        if (isPreConditionOk(state)) {
            currentState = state
            valueAnimator?.cancel()
            valueAnimator = null

            val startHeight = if (state == LeafViewState.Collapsed) height else initialHeight
            val desiredHeight =
                if (state == LeafViewState.Expanded) initialHeight + EXPAND_HEIGHT_PX else initialHeight
            valueAnimator = ValueAnimator.ofInt(
                startHeight,
                desiredHeight
            )
            valueAnimator!!.duration = ANIMATION_DURATION
            valueAnimator!!.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                layoutParams.height = value
                requestLayout()
                if (value == desiredHeight) {
                    background = getStateDrawable(state == LeafViewState.Expanded)
                    translationZ = if (state == LeafViewState.Expanded) 50f else 0f
                    function(state, expandedColor)
                }
            }
            valueAnimator!!.start()
        }
    }

    private fun isPreConditionOk(nextState: LeafViewState): Boolean {
        if (height == 0) return false
        if (nextState == currentState) return false
        if (initialHeight == 0) {
            initialHeight = height
            invalidate()
        }
        return true
    }

    private fun getStateDrawable(isExpanded: Boolean): Drawable {
        return if (!isExpanded) {
            stableDrawable
        } else {
            expandedDrawable
        }
    }

    private fun drawTitle(canvas: Canvas?) {
        if (initialHeight == 0) return
        canvas?.let {
            it.save()
            val x = initialHeight / TITLE_X_COMPENSATION
            val y = height - TITLE_Y_COMPENSATION
            it.rotate(TITLE_ROTATION_DEGREES, x, y)
            textPaint.alpha =
                if (currentState == LeafViewState.Expanded) TITLE_NO_ALPHA else TITLE_ALPHA
            it.drawText(title, x, y, textPaint)
            it.restore()
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 200L
        private const val EXPAND_HEIGHT_PX = 80
        private const val TITLE_NO_ALPHA = 255
        private const val TITLE_ALPHA = 163
        private const val TITLE_TEXT_SIZE = 93.47f
        private const val TITLE_ROTATION_DEGREES = -79f
        private const val TITLE_X_COMPENSATION = 2.7f
        private const val TITLE_Y_COMPENSATION = 20f
    }
}