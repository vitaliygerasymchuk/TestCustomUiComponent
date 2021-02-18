package app.custom.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

enum class SenseType {
    One, Two, Three, Four, NA
}

class SenseBarView : FrameLayout {

    private var card: CardView? = null
    private var senseLeaf1: SenseLeaf? = null
    private var senseLeaf2: SenseLeaf? = null
    private var senseLeaf3: SenseLeaf? = null
    private var senseLeaf4: SenseLeaf? = null
    private var backgroundCard: MaterialCardView? = null

    private var expandedSenseLeaf: SenseLeaf? = null
    private var expandedSenseType: SenseType = SenseType.NA

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

    override fun onFinishInflate() {
        super.onFinishInflate()
        card = findViewById(R.id.sense_main_card)
        senseLeaf1 = findViewById(R.id.leaf_1)
        senseLeaf2 = findViewById(R.id.leaf_2)
        senseLeaf3 = findViewById(R.id.leaf_3)
        senseLeaf4 = findViewById(R.id.leaf_4)

        backgroundCard = findViewById(R.id.background_card)

        senseLeaf1!!.expandedColor = "39BCAE"
        senseLeaf2!!.expandedColor = "#F46E37"
        senseLeaf3!!.expandedColor = "#817BDB"
        senseLeaf4!!.expandedColor = "#39B0F4"

        senseLeaf1!!.title = "Float"
        senseLeaf2!!.title = "Calm"
        senseLeaf3!!.title = "Sleep"
        senseLeaf4!!.title = "Energy"

        senseLeaf1!!.setOnClickListener {
            setExpanded(SenseType.One)
        }
        senseLeaf2!!.setOnClickListener {
            setExpanded(SenseType.Two)
        }
        senseLeaf3!!.setOnClickListener {
            setExpanded(SenseType.Three)
        }
        senseLeaf4!!.setOnClickListener {
            setExpanded(SenseType.Four)
        }
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.leafs_bar_view, this)
    }

    private fun onLeafViewStateChanged(state: SenseViewState, expandedColor: String) {
        if (state == SenseViewState.Expanded) {
            card?.setCardBackgroundColor(Color.parseColor(expandedColor))
            backgroundCard?.setCardBackgroundColor(
                getExpandedBackgroundColor()
            )
        }
    }

    fun setExpanded(type: SenseType) {
        if (type != expandedSenseType) {
            expandedSenseLeaf?.collapse(this::onLeafViewStateChanged)
        }
        expandedSenseLeaf = when (type) {
            SenseType.One -> {
                senseLeaf1?.expand(this::onLeafViewStateChanged)
                senseLeaf1
            }
            SenseType.Two -> {
                senseLeaf2?.expand(this::onLeafViewStateChanged)
                senseLeaf2
            }
            SenseType.Three -> {
                senseLeaf3?.expand(this::onLeafViewStateChanged)
                senseLeaf3
            }
            else -> {
                senseLeaf4?.expand(this::onLeafViewStateChanged)
                senseLeaf4
            }
        }
        this.expandedSenseType = type
    }

    private fun getExpandedBackgroundColor(): Int {
        val color = when (expandedSenseType) {
            SenseType.One -> {
                COLOR_FLOAT
            }
            SenseType.Two -> {
                COLOR_CALM
            }
            SenseType.Three -> {
                COLOR_SLEEP
            }
            else -> {
                COLOR_ENERGY
            }
        }
        return Color.parseColor(color)
    }

    companion object {
        private const val COLOR_ENERGY = "#896161"
        private const val COLOR_SLEEP = "#616B89"
        private const val COLOR_CALM = "#897261"
        private const val COLOR_FLOAT = "#607488"
    }
}