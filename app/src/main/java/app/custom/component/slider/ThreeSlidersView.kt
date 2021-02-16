package app.custom.component.slider

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import app.custom.component.R

class ThreeSlidersView : FrameLayout {

    var listener: OnSliderValueChangedListener? = null
    private lateinit var temperatureSlider: CustomVerticalSlider
    private lateinit var humiditySlider: CustomVerticalSlider
    private lateinit var windSpeedSlider: CustomVerticalSlider

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

    fun setTemperature(temperature: Int) {
        temperatureSlider.setValue(temperature)
    }

    fun setHumidity(humidity: Int) {
        humiditySlider.setValue(humidity)
    }

    fun setWindSpeed(windSpeed: Int) {
        windSpeedSlider.setValue(windSpeed)
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.sliders_view, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val temperatureValue = findViewById<TextView>(R.id.temperature_value)
        val humidityValue = findViewById<TextView>(R.id.humidity_value)
        val windSpeedValue = findViewById<TextView>(R.id.wind_speed_value)
        val innerListener = object : OnSliderValueChangedListener {
            override fun onSliderValueChanged(progress: Int, dataType: SliderDataType) {
                listener?.onSliderValueChanged(progress, dataType)
                val progressFormatted = dataType.formatProgress(progress)
                when (dataType) {
                    SliderDataType.Temperature -> {
                        temperatureValue.text = progressFormatted
                    }
                    SliderDataType.Humidity -> {
                        humidityValue.text = progressFormatted
                    }
                    else -> {
                        windSpeedValue.text = progressFormatted
                    }
                }
            }
        }
        temperatureSlider = findViewById(R.id.temperature_slider)
        temperatureSlider.listener = innerListener
        humiditySlider = findViewById(R.id.humidity_slider)
        humiditySlider.listener = innerListener
        windSpeedSlider = findViewById(R.id.wind_speed_slider)
        windSpeedSlider.listener = innerListener
    }
}