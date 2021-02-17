package app.custom.component

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.custom.component.shared.Loggable


class MainActivity : AppCompatActivity(), Loggable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val leafsView = findViewById<LeafsBarView>(R.id.leafs_bar_view)
        findViewById<View>(R.id.toggle1).setOnClickListener {
            leafsView.setExpanded(LeafType.One)
        }
        findViewById<View>(R.id.toggle2).setOnClickListener {
            leafsView.setExpanded(LeafType.Two)
        }
        findViewById<View>(R.id.toggle3).setOnClickListener {
            leafsView.setExpanded(LeafType.Three)
        }
        findViewById<View>(R.id.toggle4).setOnClickListener {
            leafsView.setExpanded(LeafType.Four)
        }

//        val test = findViewById<LeafView>(R.id.right)
//        test.viewTreeObserver.addOnGlobalLayoutListener {
//            if (test.height > 0) {
//                findViewById<View>(R.id.increase).setOnClickListener {
//                    test.toggle()
//                }
//            }
//        }


//        val threeSlidersView = findViewById<ThreeSlidersView>(R.id.three_sliders_view)
//        val listener = object : OnSetNewValueListener {
//            override fun onSetNewValue(value: Int, dataType: SliderDataType) {
//                when (dataType) {
//                    SliderDataType.Temperature -> {
//                        threeSlidersView.setTemperature(value)
//                    }
//                    SliderDataType.Humidity -> {
//                        threeSlidersView.setHumidity(value)
//                    }
//                    SliderDataType.WindSpeed -> {
//                        threeSlidersView.setWindSpeed(value)
//                    }
//                }
//            }
//        }
//        findViewById<SetValueView>(R.id.set_temp).listener = listener
//        findViewById<SetValueView>(R.id.set_humidity).listener = listener
//        findViewById<SetValueView>(R.id.set_wind_speed).listener = listener
    }
}