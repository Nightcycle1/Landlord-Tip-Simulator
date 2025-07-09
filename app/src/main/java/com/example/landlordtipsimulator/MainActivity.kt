package com.example.landlordtipsimulator



import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private val MINIMUM_TIP = 15
    private var monthlyRent: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- RecyclerView Setup for fences ---
        val recyclerView = findViewById<RecyclerView>(R.id.fenceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false).apply {
            initialPrefetchItemCount = 0
        }

        val listOfFenceImages = List(10) { R.drawable.icons8_fence_40 }
        recyclerView.adapter = FenceAdapter(listOfFenceImages)
        LinearSnapHelper().attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(0, 0, -10, 0)
            }
        })

        // --- Tip Slider Setup ---
        val tipSlider = findViewById<SeekBar>(R.id.tipSlider)
        val tipPercentageText = findViewById<TextView>(R.id.tipPercentageText)
        val reactionText = findViewById<TextView>(R.id.reactionText)
        val rentInput = findViewById<EditText>(R.id.rentInput)
        val tipAmountText = findViewById<TextView>(R.id.tipAmountText)

        // Add rent input listener
        rentInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                monthlyRent = try {
                    s.toString().toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
                updateTipAmount(tipSlider.progress, tipAmountText)  // <-- Add this
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tipSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val actualProgress = if (progress < MINIMUM_TIP) MINIMUM_TIP else progress
                if (progress < MINIMUM_TIP && fromUser) {
                    seekBar.progress = MINIMUM_TIP
                }

                tipPercentageText.text = "$actualProgress%"
                updateReactionText(actualProgress, reactionText)
                updateTipAmount(actualProgress, tipAmountText)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


    }

    private fun updateTipAmount(percentage: Int, tipAmountText: TextView) {
        val tipAmount = monthlyRent * percentage / 100
        tipAmountText.text = "Tip amount: $${"%.2f".format(tipAmount)}"
    }

    private fun updateReactionText(percentage: Int, textView: TextView) {
        val reaction = when (percentage) {
            in 0..14 -> {
                textView.setTextColor(Color.RED)
                "No, no, no. You can't do that."
            }
            in 15..29 -> {
                textView.setTextColor(Color.YELLOW)
                "I think we can do better next time."
            }
            in 30..49 -> {
                textView.setTextColor(Color.GREEN)
                "Good boy!"
            }
            in 50..69 -> {
                textView.setTextColor(Color.CYAN)
                "That's very sigma of you"
            }
            in 70..89 -> {
                textView.setTextColor(Color.BLUE)
                "I'll make sure to not paint over your power outlets with this amount. (Next Time)"
            }
            else -> {
                textView.setTextColor(Color.MAGENTA)
                "I'll give you a pizza next time I pass by"
            }
        }
        textView.text = reaction
    }
}

