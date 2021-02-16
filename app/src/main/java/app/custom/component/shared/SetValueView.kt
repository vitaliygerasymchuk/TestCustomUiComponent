package app.custom.component.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import app.custom.component.R
import app.custom.component.slider.SliderDataType
import java.lang.Exception

interface OnSetNewValueListener {
    fun onSetNewValue(value: Int, dataType: SliderDataType)
}

class SetValueView : FrameLayout {

    var listener: OnSetNewValueListener? = null
    private var dataType: SliderDataType = SliderDataType.Temperature

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
        LayoutInflater.from(context).inflate(R.layout.set_value_view, this)
        attrs?.let {
            val a = context.theme.obtainStyledAttributes(
                it,
                R.styleable.SetValueView,
                0, 0
            )
            try {
                dataType =
                    SliderDataType.values()[a.getInt(R.styleable.SetValueView_set_data_type, 0)]
            } finally {
                a.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val edt = findViewById<EditText>(R.id.input)
        findViewById<Button>(R.id.set).setOnClickListener {
            val value = edt.text.toString()
            try {
                listener?.onSetNewValue(value.toInt(), dataType)
            } catch (ex: Exception) {
                //
            }
        }
    }
}