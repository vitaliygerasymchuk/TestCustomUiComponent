package app.custom.component

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.custom.component.shared.OnSetNewValueListener
import app.custom.component.shared.SetValueView
import app.custom.component.slider.SliderDataType
import app.custom.component.slider.ThreeSlidersView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val threeSlidersView = findViewById<ThreeSlidersView>(R.id.three_sliders_view)
        val listener = object : OnSetNewValueListener {
            override fun onSetNewValue(value: Int, dataType: SliderDataType) {
                when (dataType) {
                    SliderDataType.Temperature -> {
                        threeSlidersView.setTemperature(value)
                    }
                    SliderDataType.Humidity -> {
                        threeSlidersView.setHumidity(value)
                    }
                    SliderDataType.WindSpeed -> {
                        threeSlidersView.setWindSpeed(value)
                    }
                }
            }
        }
        findViewById<SetValueView>(R.id.set_temp).listener = listener
        findViewById<SetValueView>(R.id.set_humidity).listener = listener
        findViewById<SetValueView>(R.id.set_wind_speed).listener = listener
    }
}