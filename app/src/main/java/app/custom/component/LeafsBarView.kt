package app.custom.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.card.MaterialCardView

enum class LeafType {
    One, Two, Three, Four, NA
}

class LeafsBarView : FrameLayout {

    private var card: MaterialCardView? = null
    private var leaf1: LeafView? = null
    private var leaf2: LeafView? = null
    private var leaf3: LeafView? = null
    private var leaf4: LeafView? = null

    private var expandedLeaf: LeafView? = null
    private var expandedLeafType: LeafType = LeafType.NA

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
        card = findViewById(R.id.leaf_main_card)
        leaf1 = findViewById(R.id.leaf_1)
        leaf2 = findViewById(R.id.leaf_2)
        leaf3 = findViewById(R.id.leaf_3)
        leaf4 = findViewById(R.id.leaf_4)

        leaf1!!.expandedColor = "39BCAE"
        leaf2!!.expandedColor = "#F46E37"
        leaf3!!.expandedColor = "#817BDB"
        leaf4!!.expandedColor = "#39B0F4"

        leaf1!!.setOnClickListener {
            setExpanded(LeafType.One)
        }
        leaf2!!.setOnClickListener {
            setExpanded(LeafType.Two)
        }
        leaf3!!.setOnClickListener {
            setExpanded(LeafType.Three)
        }
        leaf4!!.setOnClickListener {
            setExpanded(LeafType.Four)
        }
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.leafs_bar_view, this)
    }

    private fun onLeafViewStateChanged(state: LeafViewState, expandedColor: String) {
        if (state == LeafViewState.Expanded) {
            card?.setCardBackgroundColor(Color.parseColor(expandedColor))
        }
    }

    fun setExpanded(type: LeafType) {
        if (type != expandedLeafType) {
            expandedLeaf?.collapse(this::onLeafViewStateChanged)
        }
        expandedLeaf = when (type) {
            LeafType.One -> {
                leaf1?.expand(this::onLeafViewStateChanged)
                leaf1
            }
            LeafType.Two -> {
                leaf2?.expand(this::onLeafViewStateChanged)
                leaf2
            }
            LeafType.Three -> {
                leaf3?.expand(this::onLeafViewStateChanged)
                leaf3
            }
            else -> {
                leaf4?.expand(this::onLeafViewStateChanged)
                leaf4
            }
        }
        this.expandedLeafType = type
    }
}