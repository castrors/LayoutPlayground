package io.github.castrors.layoutplayground

import android.content.Context
import android.util.AttributeSet

class RadioLayout : CompoundLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun toggle() {
        if (!isChecked) {
            super.toggle()
        }
    }

}
