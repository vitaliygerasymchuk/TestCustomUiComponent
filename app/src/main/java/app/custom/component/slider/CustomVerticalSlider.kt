package app.custom.component.slider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.widget.FrameLayout
import app.custom.component.R
import app.custom.component.shared.Loggable

enum class SliderDataType {
    Temperature, Humidity, WindSpeed;

    fun formatProgress(progress: Int): String {
        val sign = when {
            this == Temperature -> {
                "°"
            }
            this == Humidity -> {
                "%"
            }
            else -> {
                "m/s"
            }
        }
        return "${progress}${sign}"
    }
}

interface OnSliderValueChangedListener {
    fun onSliderValueChanged(progress: Int, dataType: SliderDataType)
}

@Suppress("unused")
class CustomVerticalSlider : FrameLayout, Loggable {

    private var progressY: Float = 0f
    private var dataType: SliderDataType = SliderDataType.Temperature

    private lateinit var gestureDetector: GestureDetector
    private val barPaint = Paint()
    private val fillPain = Paint()

    var listener: OnSliderValueChangedListener? = null

    fun setValue(value: Int) {
        if (value < 0 || value > 100) {
            throw IllegalArgumentException("The value should be in range from 0 until 100")
        }
        progressY = fromPercentsToY(value)
        invalidate()
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        attrs?.let {
            val a = context.theme.obtainStyledAttributes(
                it,
                R.styleable.CustomVerticalSlider,
                0, 0
            )
            try {
                dataType =
                    SliderDataType.values()[a.getInt(R.styleable.CustomVerticalSlider_data_type, 0)]
            } finally {
                a.recycle()
            }
        }

        setWillNotDraw(false)
        barPaint.color = Color.LTGRAY
        fillPain.color = Color.GREEN
        gestureDetector = GestureDetector(context, object :
            GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                e2?.let {
                    if (e2.action == ACTION_MOVE) {
                        log("it.y ${it.y}")
                        progressY = when {
                            it.y < 0 -> {
                                0f
                            }
                            it.y > height -> {
                                height.toFloat()
                            }
                            else -> {
                                it.y
                            }
                        }
                        invalidate()
                    }
                }

                return true
            }
        })
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        gestureDetector.onGenericMotionEvent(event)
        return super.onGenericMotionEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBar(canvas)
        drawFill(canvas)
        listener?.onSliderValueChanged(fromYToPercents(), dataType)
    }

    private fun drawBar(canvas: Canvas) {
        canvas.drawRect(
            SIDE_PADDING,
            0f,
            getBarWidth(),
            height.toFloat(),
            barPaint
        )
    }

    private fun drawFill(canvas: Canvas) {
        canvas.drawRect(
            SIDE_PADDING,
            progressY,
            getBarWidth(),
            height.toFloat(),
            fillPain
        )
    }

    private fun fromYToPercents(): Int {
        return 100 - ((progressY * 100) / height).toInt()
    }

    private fun fromPercentsToY(percents: Int): Float {
        return ((((100 - percents) * height)) / 100f)
    }

    private fun getBarWidth(): Float {
        return width - SIDE_PADDING
    }

    companion object {
        private const val SIDE_PADDING = 10f
    }
}