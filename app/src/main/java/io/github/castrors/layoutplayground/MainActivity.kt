package io.github.castrors.layoutplayground

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var compoundLayout1: CompoundLayout
    private lateinit var compoundLayout2: CompoundLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compoundLayout1 = findViewById(R.id.profile_1)
        compoundLayout2 = findViewById(R.id.profile_2)

        bindCompoundListener(compoundLayout1, R.string.audrey_hepburn)
        bindCompoundListener(compoundLayout2, R.string.doris_day)

    }

    private fun bindCompoundListener(compoundLayout: CompoundLayout, @StringRes subtitle: Int) {
        compoundLayout.setOnCheckedChangeListener(object : CompoundLayout.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundLayout: CompoundLayout, checked: Boolean) {
                if (checked) {
                 //O COMPOUND FOI SELECIONADO
                }
            }
        })
    }

}
