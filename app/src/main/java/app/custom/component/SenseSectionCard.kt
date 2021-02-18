package app.custom.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import app.custom.component.shared.Loggable
import app.custom.component.slider.SliderDataType
import com.google.android.material.card.MaterialCardView
import org.jetbrains.annotations.TestOnly

enum class SenseCardType {
    Reality,
    Detailed,
    Abstract
}

class SenseSectionCardCard : MaterialCardView, Loggable {

    var type: SenseCardType = SenseCardType.Reality

    var imageResource: Drawable? = null
        set(value) {
            imageView.setImageDrawable(value)
            field = value
        }

    var imageUrl: String = ""
        set(value) {
            // FIXME: 18.02.2021 Glide/Picasso load image from URL
            field = value
        }


    var title: String = ""
        set(value) {
            titleView.text = value
            field = value
        }

    private lateinit var imageView: ImageView
    private lateinit var titleView: TextView

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        log("init: attrs $attrs")
        LayoutInflater.from(context).inflate(R.layout.sense_card_content, this)
        imageView = findViewById(R.id.sense_card_image)
        titleView = findViewById(R.id.sense_card_title)
        attrs?.let {
            val a = context.theme.obtainStyledAttributes(
                it,
                R.styleable.SenseSectionCard,
                0, 0
            )
            try {
                type = SenseCardType.values()[a.getInt(R.styleable.SenseSectionCard_sense_type, 0)]
                imageResource = a.getDrawable(
                    R.styleable.SenseSectionCard_sense_image_resource
                )
                title = a.getString(R.styleable.SenseSectionCard_sense_title).orEmpty()

            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                a.recycle()
            }
        }
    }
}