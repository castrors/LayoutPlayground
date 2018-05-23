package io.github.castrors.layoutplayground

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var radioLayoutGroup: RadioLayoutGroup
    private lateinit var recieveAtHomeGroup: CompoundLayout
    private lateinit var retrieveByCarGroup: CompoundLayout
    private lateinit var recieveAtHome: TextView
    private lateinit var retrieveByCar: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioLayoutGroup = findViewById(R.id.radioLayoutGroup)
        recieveAtHomeGroup = findViewById(R.id.recieveAtHomeGroup)
        retrieveByCarGroup = findViewById(R.id.retrieveByCarGroup)
        recieveAtHome = findViewById(R.id.delivery_type_recieve_at_home)
        retrieveByCar = findViewById(R.id.delivery_type_retrieve_by_car)
        button = findViewById(R.id.button)

        setupShippingSpannable(recieveAtHome, R.color.dark_gray)
        setupShippingSpannable(retrieveByCar, R.color.apple_green)

        button.setOnClickListener {
            when (radioLayoutGroup.checkedRadioLayoutId) {
                R.id.recieveAtHomeGroup -> (it as Button).text = "AAAA"
                R.id.retrieveByCarGroup -> (it as Button).text = "BBBB"
                else -> (it as Button).text = "CCCC"
            }
        }
    }

    private fun setupShippingSpannable(view: TextView, color: Int) {
        val shippingMethod = "Frete: "

        val shippingSpan = SpannableString(view.text.toString())
                shippingSpan.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, color)),
               shippingMethod.length,
                view.text.length,
                SPAN_EXCLUSIVE_EXCLUSIVE)

        view.text = SpannableStringBuilder().append(shippingSpan)
    }
}
