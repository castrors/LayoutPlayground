package io.github.castrors.layoutplayground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class CouponActivity : AppCompatActivity() {

    private lateinit var value: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon)

        value = findViewById(R.id.valueText)
    }

}
