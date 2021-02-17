package app.custom.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import app.custom.component.shared.Loggable

enum class LeafViewState {
    Expanded,
    Collapsed
}


class LeafView : FrameLayout, Loggable {

    lateinit var textView: TextView

    private var initialHeight: Int = 0
    private lateinit var stableDrawable: Drawable
    private lateinit var expandedDrawable: Drawable
    private var valueAnimator: ValueAnimator? = null
    private var pendingState: LeafViewState = LeafViewState.Collapsed

    var expandedColor: String = "#ffffff"
        set(value) {
            field = if (!value.contains("#")) {
                "#${value}"
            } else {
                value
            }
            DrawableCompat.setTint(expandedDrawable, Color.parseColor(field))
        }

    var title: String = ""
        set(value) {
            textView.text = value
            field = value
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

    fun expand(function: (state: LeafViewState, expandedColor: String) -> Unit) {
        setState(LeafViewState.Expanded, function)
    }

    fun collapse(function: (state: LeafViewState, expandedColor: String) -> Unit) {
        setState(LeafViewState.Collapsed, function)
    }

    private fun init() {
        stableDrawable = ContextCompat.getDrawable(context, R.drawable.ic_leaf)!!
        expandedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_leaf)!!.mutate()

        background = stableDrawable
        textView = TextView(context)
    }

    private fun setState(
        state: LeafViewState,
        function: (state: LeafViewState, expandedColor: String) -> Unit
    ) {
        if (isPreConditionOk(state)) {
            pendingState = state
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
        if (nextState == pendingState) return false
        if (initialHeight == 0) {
            initialHeight = height
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

    companion object {
        private const val ANIMATION_DURATION = 200L
        private const val EXPAND_HEIGHT_PX = 80
    }
}