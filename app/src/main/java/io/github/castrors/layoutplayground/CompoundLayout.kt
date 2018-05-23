package io.github.castrors.layoutplayground

import android.content.Context
import android.util.AttributeSet
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout

open class CompoundLayout : FrameLayout, Checkable {

    private var mChecked: Boolean = false

    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    private var mOnCheckedChangeWidgetListener: OnCheckedChangeListener? = null

    private var mBroadcasting: Boolean = false

    constructor(context: Context) : super(context) {
        initialize(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initialize(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null) {
            val styleAttributes = context.obtainStyledAttributes(
                    attrs, R.styleable.CompoundLayout, defStyleAttr, defStyleRes)
            val checked = styleAttributes.getBoolean(
                    R.styleable.CompoundLayout_checked, false)
            isChecked = checked
            styleAttributes.recycle()
        }

        isClickable = true
    }

    override fun performClick(): Boolean {
        toggle()

        val handled = super.performClick()
        if (!handled) {
            playSoundEffect(SoundEffectConstants.CLICK)
        }

        return handled
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked != checked) {
            mChecked = checked
            refreshDrawableState()

            if (mBroadcasting) {
                return
            }

            mBroadcasting = true
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener!!.onCheckedChanged(this, mChecked)
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener!!.onCheckedChanged(this, mChecked)
            }

            mBroadcasting = false
        }
    }

    override fun isChecked(): Boolean {
        return this.mChecked
    }

    override fun toggle() {
        isChecked = !isChecked
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener
    }

    internal fun setOnCheckedChangeWidgetListener(onCheckedChangeWidgetListener: OnCheckedChangeListener?) {
        this.mOnCheckedChangeWidgetListener = onCheckedChangeWidgetListener
    }


    interface OnCheckedChangeListener {
        fun onCheckedChanged(compoundLayout: CompoundLayout, checked: Boolean)
    }

    companion object {

        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }

}
